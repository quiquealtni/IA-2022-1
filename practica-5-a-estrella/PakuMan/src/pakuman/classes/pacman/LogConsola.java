/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.util.logging.ConsoleHandler;

/**
 *
 * @author blackzafiro
 */
public class LogConsola extends ConsoleHandler {
	
	public LogConsola() {
		this.setOutputStream(System.out);
	}
}
