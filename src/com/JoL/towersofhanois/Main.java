package com.JoL.towersofhanois;

import java.util.Stack;

public class Main {
	private byte numRings = 6;
	private Ring[] rings;
	private Byte[] best;

	private Stack<Integer[]> tops = new Stack<Integer[]>();
	private Stack<Byte>[] sticks = new Stack[3];
	private Stack<Byte> moves = new Stack<Byte>();
	
	public Main() {
		rings = new Ring[numRings];
		for (int i = 0; i < sticks.length; i++) {
			sticks[i] = new Stack<Byte>();
		}
		for (byte i = 0; i < rings.length; i++) {
			rings[i] = new Ring(i);
			sticks[0].push((byte) (numRings-i - 1));
		}

		populateTops();
		tree(-1);
		printSolution();
	}
	
	public void tree(int lastTo) {
		for (byte i = 0; i <= 2; i++) {
			if (i == lastTo) continue;
			for (byte j = 0; j <= 2; j++) {
				if (move(i,j)) {
					populateTops();
					moves.push((byte) (i | (j << 2)));
					
					if (sticks[2].size() == numRings) {
						if (best == null || moves.size() < best.length) best = moves.toArray(new Byte[moves.size()]);
					} else {
						tree(j);
					}
					
					sticks[i].push(sticks[j].pop());
					moves.pop();
					tops.pop();
				}
			}
		}
		
		
	}
	
	public void printSolution() {
		for (Byte move : best) {
			byte to = (byte) ((move >> 2) & 0b11);
			byte from = (byte) (move & 0b11);
			System.out.println(from + " to " + to);
		}
	}
	
	public boolean move(int from, int to) {
		if (sticks[from].isEmpty() || from == to) return false;
		if (sticks[to].isEmpty() || rings[sticks[to].peek()].size > rings[sticks[from].peek()].size) {
			sticks[to].push(sticks[from].pop());
			if (contains()) {
				sticks[from].push(sticks[to].pop());
				return false;
			}

			return true;
		}
		
		return false;
	}
	
	public boolean contains() {
		byte stickTop0 = sticks[0].size() == 0 ? -1 : sticks[0].peek();
		byte stickTop1 = sticks[1].size() == 0 ? -1 : sticks[1].peek();
		byte stickTop2 = sticks[2].size() == 0 ? -1 : sticks[2].peek();
		int stickSize0 = sticks[0].size();
		int stickSize1 = sticks[1].size();
		int stickSize2 = sticks[2].size();
		int stick0 = (stickTop0 & 0b11) | ((stickSize0 << 2) & (~0b11));
		int stick1 = (stickTop1 & 0b11) | ((stickSize1 << 2) & (~0b11));
		int stick2 = (stickTop2 & 0b11) | ((stickSize2 << 2) & (~0b11));
		
		for (Integer[] b : tops) {
			if (stick0 == b[0] && stick1 == b[1] && stick2 == b[2]) {
				return true;
			}
		}
		return false;
	}
	
	public void populateTops() {
		byte stickTop0 = sticks[0].size() == 0 ? -1 : sticks[0].peek();
		byte stickTop1 = sticks[1].size() == 0 ? -1 : sticks[1].peek();
		byte stickTop2 = sticks[2].size() == 0 ? -1 : sticks[2].peek();
		int stickSize0 = sticks[0].size();
		int stickSize1 = sticks[1].size();
		int stickSize2 = sticks[2].size();
		tops.push(new Integer[] {(stickTop0 & 0b11) | ((stickSize0 << 2) & (~0b11)), (stickTop1 & 0b11) | ((stickSize1 << 2) & (~0b11)), (stickTop2 & 0b11) | ((stickSize2 << 2) & (~0b11)),});
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
