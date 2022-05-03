/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module pakuman {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	
	opens pacman to javafx.fxml;
	
	exports pacman;
	exports pacman.comida;
	exports pacman.escenario;
	exports pacman.personajes;
}
