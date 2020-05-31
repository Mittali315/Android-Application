package com.paril.mlaclientapp.ui.activity;


import java.util.ArrayList;

public class Wallet {

	
	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static int difficulty = 2;
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
	

}