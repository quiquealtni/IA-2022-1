/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author blackzafiro
 */
public class Formateador extends SimpleFormatter {
	
	public String format(LogRecord record) {
		return String.format("[%s]: %s%n", record.getLoggerName(),
		                                 record.getMessage());
	}
}
