/*******************************************************************************
 * Copyright (C) 2008  CS-Computer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     CS-Computer - initial API and implementation
 ******************************************************************************/
package it.archiworld.archman.client.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public abstract class WordDataSourceGenerator {

	private String filename;

	private FileOutputStream file;

	public WordDataSourceGenerator(String filename) {
		this.filename = filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return this.filename;
	}

	public abstract String fetchLine();

	public void generate() {
		try {
			this.file = new FileOutputStream(this.filename);

			PrintStream stream = new PrintStream(file);
			String line;
			while ((line = fetchLine()) != null) {
				stream.println(line);
			}

			file.close();
			System.out
					.println("WordDataSourceGenerator: DataSource written to disk."); //$NON-NLS-1$

		} catch (IOException e) {
			System.out
					.println("WordDataSourceGenerator: Error opening output file."); //$NON-NLS-1$
		}
	}
}
