package com.battleship.main;

import com.battleship.database.Player;
import com.battleship.design.Game;
import com.battleship.design.FormLogin;

public class Main {
	public static void main(String[] args) {
		new Game(new Player("Tomás", "1234", "email@example.com"));
		//new FormLogin();
	}
}
