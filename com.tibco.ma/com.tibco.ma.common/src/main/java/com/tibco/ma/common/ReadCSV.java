package com.tibco.ma.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.opencsv.CSVReader;

/**
 * @author Daniel
 */
public class ReadCSV {
	private CSVReader reader; // read csv file
	private FileReader fr;
	private InputStreamReader isReader;
	private String path; // file path
	private boolean isfirst;
	private int colNum;
	private List<String[]> row;

	public ReadCSV(String path) {
		this.path = path;
		try {
			isReader = new InputStreamReader(new FileInputStream(path), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// fr = new FileReader(path);
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// reader = new CSVReader(fr);
		reader = new CSVReader(isReader);
	}

	public String getPath() {
		return this.path;
	}

	public List<String[]> getRowValues() {
		if (isfirst) {
			isfirst = false;
			return row;
		}
		try {
			// row = reader.readNext();
			row = reader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return row;
	}

	public int getColNum() {
		getRowValues();
		isfirst = true;
		if (row != null)
			colNum = row.size();
		return colNum;
	}

	public void close() {
		try {
			if (fr != null) {
				fr.close();
				fr = null;
			}
			if (reader != null) {
				reader.close();
				reader = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
