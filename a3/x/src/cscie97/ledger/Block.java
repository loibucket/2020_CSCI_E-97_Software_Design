package com.cscie97.ledger;

import java.util.List;
import java.util.Map;

/**
 * The Block aggregates groups of 10 transactions. Transactions should be added
 * to blocks in the order that they are received. Prior to adding a transaction
 * to a block, the transaction must be validated. Transactions that are invalid
 * should be discarded. The block also contains an account balance map that
 * reflects the balance of all accounts after all the transactions within the
 * block have been applied. The account balance map should be copied from the
 * previous block and updated to reflect the transactions in the current block.
 * The block contains the hash of the previous block. It also contains the hash
 * of itself. Final to prevent modifications like additional methods
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public final class Block {

    /**
     * A sequentially incrementing block number assigned to the block. The first
     * block or genesis block is assigned a block number of 1.
     */
    private final int blockNumber;
    /**
     * The hash of the previous block in the blockchain. For the genesis block, this
     * is empty. Use the Sha-256 algorithm and Merkle tree to compute the hash per
     * the requirements.
     */
    private final String previousHash;
    /**
     * The hash of the current block is computed based on all properties and
     * associations of the current Block except for this attribute. Use the Sha-256
     * algorithm and Merkle tree to compute the hash per the requirements.
     */
    private final String hash;

    /**
     * An ordered list of Transactions that are included in the current block. There
     * should be exactly 10 transactions per block.
     */
    private final List<Transaction> transactionList;

    /**
     * The full set of accounts managed by the Ledger. The account balances should
     * reflect the account state after all transactions of the current block have
     * been applied. Note that each Block has its own immutable copy of the
     * accountBalanceMap.
     */
    private final Map<String, Account> accountBalanceMap;

    /**
     * The previousBlock association references the preceding Block in the
     * blockchain. private final Block previousBlock;
     */
    private final Block previousBlock;

    public int getBlockNumber() {
        return this.blockNumber;
    }

    // get previous hash
    public String getPreviousHash() {
        return previousHash;
    }

    // get hash
    public String getHash() {
        return hash;
    }

    // get transaction list
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    // get account balances
    public Map<String, Account> geAccountBalanceMap() {
        return accountBalanceMap;
    }

    // get pervious block
    public Block getPreviousBlock() {
        return previousBlock;
    }

    // create a block
    public Block(int blockNumber, String hash, List<Transaction> transactionPool,
            Map<String, Account> accountBalancePool, Block previousBlock, String previousBlockHash) {
        this.blockNumber = blockNumber;
        this.previousHash = previousBlockHash;
        this.transactionList = transactionPool;
        this.accountBalanceMap = accountBalancePool;
        this.previousBlock = previousBlock;
        this.hash = hash;
    }

    // print out of the block
    public String toString() {
        return ("------------\nBLOCK NUMBER: " + this.blockNumber + "\nBLOCK HASH: " + this.hash + "\nPREVIOUS HASH: "
                + this.previousHash + "\nTRANSACTIONS: " + this.transactionList.toString().replaceAll(",|\\[|\\]", "")
                + "\nACCOUNT BALANCES: " + this.accountBalanceMap.values().toString().replaceAll(",|\\[|\\]", "")
                + "\n------------");
    }
}
