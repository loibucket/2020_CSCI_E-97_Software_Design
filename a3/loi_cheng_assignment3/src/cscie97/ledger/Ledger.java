package cscie97.ledger;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * The Ledger manages the Blocks of the blockchain. It also provides the API
 * used by clients of the Ledger. The Ledger processes transaction processing
 * requests, and also queries about the state of the Ledger, including Account
 * balances, Transaction details, and Block details. Final to prevent
 * modifications like additional methods
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public final class Ledger {

    // Name of the ledger.
    private String name;
    // Ledger description.
    private String description;
    // The Seed that is used as input to the hashing algorithm.
    private String seed;

    // The initial Block of the blockchain.
    private Block genesisBlock;
    // A map of block numbers and the associated
    private Map<Integer, Block> blockMap;

    // the transactionPool and balancePool are temporary "blocks" that hold the data
    // until ready to be committed to a complete block
    private List<Transaction> transactionPool;
    private Map<String, Account> balancePool;

    // the max number of coins possible in the ledger
    private static final int ALL_COINS = 2147483647;

    // get the blockMap;
    public Map<Integer, Block> getBlockMap() {
        return this.blockMap;
    }

    // get description;
    public String getDescription() {
        return description;
    }

    // get seed;
    public String getSeed() {
        return seed;
    }

    // get balance pool
    public Map<String, Account> getBalancePool() {
        return balancePool;
    }

    /**
     * initialize the ledger
     *
     * @param name
     * @param description
     * @param seed
     * @throws LedgerException
     */
    public Ledger(String name, String description, String seed) throws LedgerException {
        this.name = name;
        this.description = description;
        this.seed = seed;
        this.blockMap = new HashMap<Integer, Block>();
        this.balancePool = new HashMap<String, Account>();
        this.transactionPool = new ArrayList<Transaction>();
        // make master account
        Account master = new Account("master");
        master.updateBalance(ALL_COINS);
        this.balancePool.put("master", master);
    }

    /**
     * Create a new account, assign a unique identifier, and set the balance to 0.
     * Return the account identifier.
     *
     * @param accountId
     * @return
     * @throws LedgerException
     */
    public String createAccount(String accountId) throws LedgerException {
        if (this.balancePool.keySet().contains(accountId)) {
            throw new LedgerException("create-account", "account already exists");
        }
        this.balancePool.put(accountId, new Account(accountId));
        return accountId;
    }

    /**
     * Process a transaction. Validate the Transaction and if valid, add the
     * Transaction to the current Block and update the associated Account balances
     * for the current Block. Return the assigned transaction id. If the transaction
     * is not valid, throw a LedgerException.
     *
     * @param transaction
     * @return
     * @throws LedgerException
     */
    public String processTransaction(Transaction transaction) throws LedgerException {
        if (transaction.payer.getBalance() < transaction.amount + transaction.fee) {
            throw new LedgerException("process-transaction", "insufficient funds to cover the amount plus fee");
        }
        if (transaction.fee < 10) {
            throw new LedgerException("process-transaction", "fee must be at least 10");
        }
        transaction.payer.updateBalance(transaction.payer.getBalance() - transaction.amount - transaction.fee);
        transaction.receiver.updateBalance(transaction.receiver.getBalance() + transaction.amount);
        balancePool.get("master").updateBalance(balancePool.get("master").getBalance() + transaction.fee);
        this.transactionPool.add(transaction);
        // after 10 transactions, write to a new block
        if (this.transactionPool.size() == 10) {
            commitBlock();
        }
        return transaction.transactionId;
    }

    // helper function to commit block
    @SuppressWarnings("unchecked")
    private void commitBlock() throws LedgerException {
        String blockHash = treeHash(this.transactionPool, this.balancePool);
        if (this.blockMap.isEmpty()) {
            // genesis block
            genesisBlock = new Block(1, blockHash, transactionPool, balancePool, null, null);
            this.blockMap.put(1, genesisBlock);
        } else {
            // all other blocks
            Block latestBlock = this.blockMap.get(Collections.max(this.blockMap.keySet()));
            Block newBlock = new Block(latestBlock.getBlockNumber() + 1, blockHash, transactionPool, balancePool,
                    latestBlock, latestBlock.getHash());
            this.blockMap.put(newBlock.getBlockNumber(), newBlock);
        }
        // rebuild the transaction pool and balance pool
        this.transactionPool = new ArrayList<Transaction>();
        this.balancePool = (Map<String, Account>) deepCopy(balancePool);
    }

    /**
     * Return the account balance for the Account with a given address based on the
     * most recent completed block. If the Account does not exist, throw a
     * LedgerException.
     *
     * @param accountId
     * @return
     * @throws LedgerException
     */
    public int getAccountBalance(String accountId) throws LedgerException {
        // check if any complete block exists
        if (this.blockMap.keySet().isEmpty()) {
            throw new LedgerException("get-account-balance", "no completed blocks found in ledger");
        }
        // get most recent complete block
        Block completeBlock = this.blockMap.get(Collections.max(this.blockMap.keySet()));
        // check if key exists
        Map<String, Account> completeAccts = completeBlock.geAccountBalanceMap();
        if (!completeAccts.keySet().contains(accountId)) {
            throw new LedgerException("get-account-balance", "account not found in most recent completed block");
        }
        Account account = completeAccts.get(accountId);
        return account.getBalance();
    }

    /**
     * Return the account balance for the Account with a given address based on the
     * most recent completed block. If the Account does not exist, throw a
     * LedgerException.
     *
     * @return
     * @throws LedgerException
     */
    public Map<String, Integer> getAccountBalances() throws LedgerException {
        // check if any complete block exists
        if (this.blockMap.keySet().isEmpty()) {
            throw new LedgerException("get-account-balances", "no completed blocks found in ledger");
        }
        // retrieve account balances from most recent completed block
        // rewrite account balance map into <String, Integer> format
        Map<String, Account> acctBalMap = this.blockMap.get(Collections.max(this.blockMap.keySet()))
                .geAccountBalanceMap();
        Map<String, Integer> acctBal = new HashMap<String, Integer>();
        for (String key : acctBalMap.keySet()) {
            acctBal.put(key, acctBalMap.get(key).getBalance());
        }
        return acctBal;
    }

    /**
     * Return the Block for the given block number.
     *
     * @param blockNumber
     * @return
     * @throws LedgerException
     */
    public Block getBlock(int blockNumber) throws LedgerException {
        Block block = null;
        try {
            block = this.blockMap.get(blockNumber);
        } catch (Exception e) {
            throw new LedgerException("get-block", "block not found");
        }
        return block;
    }

    /**
     * Return the Transaction for the given transaction id.
     *
     * transaction ids 1-10 are in block 1, 11-20 in block 2, etc.. ; each
     * transaction index 0 holds transacton id 1,11 or 21...etc. index 2 holds 2,
     * 12, or 22, ...etc.;
     *
     * @param transactionId
     * @return
     * @throws LedgerException
     */
    public Transaction getTransaction(String transactionId) throws LedgerException {
        int transId = Integer.parseInt(transactionId);
        int blockNum = (int) Math.ceil(transId / 10.0);
        Transaction transaction = null;
        try {
            transaction = this.blockMap.get(blockNum).getTransactionList().get(transId - 1 - (blockNum - 1) * 10);
        } catch (Exception e) {
            throw new LedgerException("get-transaction", "transaction not found");
        }
        return transaction;
    }

    /**
     * Validate the current state of the blockchain. For each block, check the hash
     * of the previous hash, make sure that the account balances total to the max
     * value. Verify that each completed block has exactly 10 transactions.
     *
     * @throws LedgerException
     */
    public void validate() throws LedgerException {
        if (this.blockMap.isEmpty()) {
            throw new LedgerException("validate", "no blocks in ledger to validate");
        }
        // validate each block
        for (int i = 2; i <= blockMap.keySet().size(); i++) {
            Block block = blockMap.get(i);
            Block previousBlock = blockMap.get(i - 1);
            // check that the previous block hashes match
            if (!block.getPreviousHash().equals(previousBlock.getHash())) {
                throw new LedgerException("validate", "block:" + block.getBlockNumber()
                        + " stored previous hash does not match the hash stored in the previous block");
            }
            if (!block.getPreviousHash()
                    .equals(treeHash(previousBlock.getTransactionList(), previousBlock.geAccountBalanceMap()))) {
                throw new LedgerException("validate", "block:" + block.getBlockNumber()
                        + " stored previous hash does not match actual previous hash");
            }
            // check there are 10 transactions
            if (block.getTransactionList().size() != 10) {
                throw new LedgerException("validate",
                        "block:" + block.getBlockNumber() + " does not contain exactly 10 transactions");
            }
            // check the account balances add up to all coins
            int coins = 0;
            for (String key : block.geAccountBalanceMap().keySet()) {
                coins = coins + block.geAccountBalanceMap().get(key).getBalance();
            }
            if (coins != ALL_COINS) {
                throw new LedgerException("validate", "block:" + block.getBlockNumber()
                        + " the total in all balances is unequal to all coins in existence");
            }
        }
    }

    // gets the ledger name as a string
    public String toString() {
        return (this.name);
    }

    /**
     * helper to create the block hash
     *
     * @param transList
     * @param acctMap
     * @return
     * @throws LedgerException
     */
    private String treeHash(List<Transaction> transList, Map<String, Account> acctMap) throws LedgerException {
        // check if there are 10 transactions
        if (transList.size() != 10) {
            throw new LedgerException("process-transaction",
                    "ledger attempted to hash a list of transactons not equal to 10");
        }
        // hash transactions, add to list
        List<byte[]> hashList = new ArrayList<byte[]>();
        for (int i = 0; i < 10; i++) {
            hashList.add(objectHash(transList.get(i)));
        }
        // hash account map, add to list twice
        byte[] acctHash = objectHash(acctMap);
        hashList.add(acctHash);
        hashList.add(acctHash);

        // build the hash tree per hashtree.xlsx

        // build level 3 tree nodes
        List<byte[]> level3hash = new ArrayList<byte[]>();
        for (int i = 0; i < 6; i++) {
            byte[] a = hashList.get(i * 2);
            byte[] b = hashList.get(i * 2 + 1);
            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
            level3hash.add(objectHash(c));
        }

        // build level 2 tree nodes
        List<byte[]> level2hash = new ArrayList<byte[]>();
        for (int i = 0; i < 3; i++) {
            byte[] a = level3hash.get(i * 2);
            byte[] b = level3hash.get(i * 2 + 1);
            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
            level2hash.add(objectHash(c));
        }

        // build level 1 tree nodes
        byte[] a = level2hash.get(0);
        byte[] b = level2hash.get(1);
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        byte[] level1hash = objectHash(c);

        // build final tree hash
        a = level1hash;
        b = level2hash.get(2);
        c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return bytesToHex(objectHash(c));
    }

    /**
     * helper to serialize an object into a byte array, create the hash of the
     * serialized object, return the hash as a hex string
     *
     * @param obj
     * @return
     * @throws LedgerException
     */
    private byte[] objectHash(Object obj) throws LedgerException {
        byte[] hash;
        // seralize object
        // https://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        // hash object with seed
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(seed.getBytes(StandardCharsets.UTF_8));
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            hash = digest.digest(bos.toByteArray());
            bos.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println(e);
            throw new LedgerException("hash object", "error encoutered during hashing");
        }
        return hash;
    }

    /**
     * helper to convert byte array to hex string
     * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Makes a deep copy of any Java object that is passed.
     * https://www.journaldev.com/17129/java-deep-copy-object
     *
     * @throws LedgerException
     */
    private static Object deepCopy(Object object) throws LedgerException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        } catch (Exception e) {
            throw new LedgerException("deepCopy");
        }
    }
}
