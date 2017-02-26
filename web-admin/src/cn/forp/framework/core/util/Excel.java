/**
 * Copyright © 2016, Forp Co., LTD
 *
 * All Rights Reserved.
 */
package cn.forp.framework.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Microsoft Excel报表工具类
 *
 * @author	Bruce
 * @version	2012-3-19 09:48:46
 */
public class Excel
{
	/**
	 * Log4j lg
	 */
	private final static Logger lg = LoggerFactory.getLogger(Excel.class);
	
	public static final String SPLIT_STR = "##";
	/**
	 * 上边框
	 */
	public static final int BORDER_TOP = 0;
	/**
	 * 下边框
	 */
	public static final int BORDER_BOTTOM = 1;
	/**
	 * 左边框
	 */
	public static final int BORDER_LEFT = 2;
	/**
	 * 右边框
	 */
	public static final int BORDER_RIGHT = 3;

	private POIFSFileSystem fileSystem = null;
	private HSSFWorkbook workBook = null;
	private HSSFSheet sheet = null;
	private int currentSheet = -1;
	private HSSFPatriarch patriarch = null;

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 * @throws Exception 
	 */
	public Excel(String filePath) throws Exception
	{
		//读取模板文件
		InputStream is = new FileInputStream(filePath);
		fileSystem = new POIFSFileSystem(is);
		workBook = new HSSFWorkbook(fileSystem);
	}
	
	/**
	 * 设置当前工作使用的sheet页。
	 */
	public void setCurrentSheet(int sheetNumber)
	{
		currentSheet = sheetNumber - 1;
		sheet = workBook.getSheetAt(currentSheet);
	}

	/**
	 * 获取指定单元格的字符
	 * 
	 * @param rowNumber
	 * @param colNumber
	 * @return
	 */
	public String getText(int rowNumber, int colNumber)
	{
//		return sheet.getRow(rowNumber - 1).getCell((short) (colNumber - 1)).getRichStringCellValue().getString();
		return sheet.getRow(rowNumber - 1).getCell(colNumber - 1).getRichStringCellValue().getString();
	}
	
	/**
	 * 隐藏指定列
	 * 
	 * @param colNumber		列号
	 * @return
	 */
	public void hideColumn(short colNumber)
	{
		sheet.setColumnWidth(colNumber - 1, 0);
	}

	/**
	 * 隐藏指定列
	 * 
	 * @param colNumber		列号
	 * @return
	 */
	public void hideRow(int rowNumber)
	{
		HSSFRow row = sheet.getRow(rowNumber - 1);
		row.setHeight((short)0);
	}

	/**
	 * 设置行高
	 * 
	 * @param rowNumber
	 */
	public void setRowHeight(int rowNumber, short height)
	{
		HSSFRow row = sheet.getRow(rowNumber - 1);
		row.setHeight((short) (height * 20));
	}
	
	/**
	 * 在指定行列处插入多行数据
	 * 
	 * @param rowNumber	起始行号
	 * @param colNumber	起始列号
	 */
	public void insert(int rowNumber, int colNumber, String[] data)
	{
		if (null == data)
		{
			lg.warn("要保存的数据为Null，无法保存！");
			return;
		}
		
		if (0 == data.length)
			return;
		else
		{
			//处理第一行记录。
			HSSFRow row = sheet.getRow(rowNumber - 1);
			int maxColums = row.getPhysicalNumberOfCells();
			String[] cellData = data[0].split(SPLIT_STR);

			//如果插入数据的列与最大的列数不相符，则使用2者中较小的一个。
			if (maxColums > cellData.length)
				maxColums = cellData.length;
			
			//插入首行数据
			HSSFCell cell = null;
			for (int i = 0; i < maxColums; i++)
			{
				cell = row.getCell(colNumber - 1 + i);
				//插入单元格数据
				setCellValue(cell, cellData[i]);
			}

			//获取模板中第一行设置的样式列表。
			HSSFCellStyle[] cellStyle = new HSSFCellStyle[maxColums];
			for (int i = 0; i < maxColums; i++)
			{
				cellStyle[i] = row.getCell(colNumber - 1 + i).getCellStyle();
				//lg.debug("column:" + (i + 1) + "；align:" + cellStyle[i].getAlignment());
			}
			
			//插入剩余行数据
			for (int i = 1; i < data.length; i++)
			{
				lg.debug("row:" + (rowNumber + i));
				//后移一行后续行记录，然后在当前位置插入一行。
				sheet.shiftRows(rowNumber + i - 1, sheet.getPhysicalNumberOfRows(), 1);
				row = sheet.createRow(rowNumber + i - 1);
				cellData = data[i].split(SPLIT_STR);
				for (int j = 0; j < maxColums; j++)
				{
					//lg.debug("cell:" + (rowNumber + i) + "," + (j + 1));
					//lg.debug("sample column:" + (j + 1) + "；align:" + cellStyle[j].getAlignment());
					
					cell = row.createCell(colNumber - 1 + j);
					//同步单元格格式
					setCellStyle(cell, cellStyle[j]);

					//插入单元格数据
					setCellValue(cell, cellData[j]);
				}
			}
//			//建立最后一行，将顶部的线条设为粗线条
//			row = sheet.createRow(rowNumber + data.length - 1);
//			row.setHeight((short)20);
//			for (int j = 0; j < columNumber; j++)
//			{
//				cell = row.createCell((short)j);
//				cell.getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
//			}
				
			//设置打印区域
			//TODO [待完善...] 暂时只处理列数<=Z的情况，其它情况后面处理。
//			char columInLetter = (char)(maxColums + 64);
//			lg.debug("Last ColumInLetter:" + columInLetter);
//			workBook.setPrintArea(currentSheet, "$A$1:$" + columInLetter + "$" + sheet.getPhysicalNumberOfRows());
		}
	}
	
	/**
	 * 设置单元格数据（区分单元格数据类型）
	 * 
	 * @param cell			单元格
	 * @param cellData		单元格值
	 */
	private void setCellValue(HSSFCell cell, String cellData)
	{
		try
		{
			//整数,小数，货币（带格式）
			if (189 == cell.getCellStyle().getDataFormat() || 190 == cell.getCellStyle().getDataFormat() ||
				188 == cell.getCellStyle().getDataFormat() || 7 == cell.getCellStyle().getDataFormat() ||
				185 == cell.getCellStyle().getDataFormat())
			{
				if (StringUtils.isNotBlank(cellData))
				{
					cell.setCellValue(Double.parseDouble(cellData));
				}
			}
			else
			{
				lg.debug("普通文本格式：" + cell.getCellStyle().getDataFormat());
				cell.setCellValue(cellData);	//普通字符形式
			}
		}
		catch (Exception e)
		{
			lg.error("报表单元格数据格式转换错误：" + cell.getCellStyle().getDataFormat(), e);
			//默认为普通的字符形式
			cell.setCellValue(cellData);
		}
	}

	/**
	 * 设置单元格格式，解除每一列的样式对象引用，为单个单元格的样式修改准备条件。
	 * 
	 * @param cell		单元格
	 * @param style		模板单元格格式
	 */
	private void setCellStyle(HSSFCell cell, HSSFCellStyle cellStyle)
	{
		//暂时使用样式单元格的样式对象来格式化新的单元格信息，避免大数据量环境下产生的“字体个数超出限制”错误。
		cell.setCellStyle(cellStyle);
		
//		//边框，文字对其方式
//		HSSFCellStyle newCellStyle = workBook.createCellStyle();
//		newCellStyle.setBorderTop(cellStyle.getBorderTop());
//		newCellStyle.setBorderBottom(cellStyle.getBorderBottom());
//		newCellStyle.setBorderLeft(cellStyle.getBorderLeft());
//		newCellStyle.setBorderRight(cellStyle.getBorderRight());
//		newCellStyle.setVerticalAlignment(cellStyle.getVerticalAlignment());
//		newCellStyle.setAlignment(cellStyle.getAlignment());
//
//		//字体信息
//		HSSFFont styleFont = workBook.getFontAt(cellStyle.getFontIndex());
//		HSSFFont newFont = workBook.createFont();
//		newFont.setColor(styleFont.getColor());
//		newFont.setBoldweight(styleFont.getBoldweight());
//		newFont.setFontHeight(styleFont.getFontHeight());
//		newFont.setFontHeightInPoints(styleFont.getFontHeightInPoints());
//		newFont.setFontName(styleFont.getFontName());
//		newFont.setItalic(styleFont.getItalic());
//		newFont.setStrikeout(styleFont.getStrikeout());
//		newFont.setTypeOffset(styleFont.getTypeOffset());
//		newFont.setUnderline(styleFont.getUnderline());
//		newCellStyle.setFont(newFont);
//
//		cell.setCellStyle(newCellStyle);
	}

	/**
	 * 在指定的cell中插入文本字符
	 * 
	 * @param row		行
	 * @param column	列
	 * @param text		文本内容
	 */
	public void insert(int row, int column, String text)
	{
		if (StringUtils.isBlank(text))
			return;
		else
		{
			HSSFRow rows = sheet.getRow(row - 1);
			if (null != rows)
			{
				HSSFCell cell = rows.getCell(column - 1);
				if (null != cell)
				{
					//获取原样式
//					HSSFCellStyle style = cell.getCellStyle();
//					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//插入单元格数据
					setCellValue(cell, text);
					//恢复原样式
//					cell.setCellStyle(style);					
				}
			}
		}
	}

	/**
	 * 置空单元格内容
	 */
	public void emptyCellText(int row, int column)
	{
		HSSFRow rows = sheet.getRow(row - 1);
		if (null != rows)
		{
			HSSFCell cell = rows.getCell(column - 1);
			if (null != cell)
				cell.setCellValue("");
		}
	}

	/**
	 * 使用已有行的格式建立新行
	 * 
	 * @param row					建立新行的行号
	 * @param styleRow			格式行行号
	 * @param copyValue		是否拷贝单元格值
	 */
	public void createRow(int row, int styleRowNumber, boolean copyValue)
	{		
		//获取格式行信息
		HSSFRow rowInfo = sheet.getRow(styleRowNumber - 1);
		int columNumber = rowInfo.getPhysicalNumberOfCells();
		HSSFCellStyle[] cellStyle = new HSSFCellStyle[columNumber];
		for (int i = 0; i < columNumber; i++)
			cellStyle[i] = rowInfo.getCell(i).getCellStyle();
		
		HSSFCell cell = null;
		rowInfo = sheet.createRow(row - 1);
		for (int j = 0; j < columNumber; j++)
		{
			cell = rowInfo.createCell(j);
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellStyle(cellStyle[j]);
			if (copyValue)
				setCellValue(cell, getCellValue(styleRowNumber, (short) (j + 1)));
		}
	}

	/**
	 * 使用已有行的格式建立新行
	 * 
	 * @param column				新的列号
	 * @param styleColumnNumber		格式列列号
	 * @param width					列宽度
	 */
	public void createColumn(short column, int styleColumnNumber)
	{
		//TODO [待完善...] 暂时不考虑插入一列的需求，只考虑在最后一列后面新建列。

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFCellStyle cellStyle = null;
		for (Iterator<?> itr = sheet.rowIterator(); itr.hasNext(); )
		{
			row = (HSSFRow) itr.next();
			cellStyle = row.getCell(styleColumnNumber - 1).getCellStyle();
			cell = row.createCell(column - 1);
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellStyle(cellStyle);
		}

		sheet.setColumnWidth(column - 1, sheet.getColumnWidth(column - 2));
	}

	/**
	 * 合并指定行的多个单元格
	 * 
	 * @param row			行号
	 * @param startColumn	开始列号
	 * @param endColumn		结束列号
	 */
	public void mergeCell(int fromRow, int fromColumn, int toRow, int toColumn) 
	{
		try
		{
			CellRangeAddress region = new CellRangeAddress(fromRow - 1, toRow - 1, (fromColumn - 1), (toColumn - 1));
			sheet.addMergedRegion(region);
			//sheet.addMergedRegion(new Region(fromRow - 1, (short) (fromColumn - 1), toRow - 1, (short) (toColumn - 1)));
		}
		catch (Exception e)
		{
			lg.warn("单元格合并错误：" + e.getMessage());
		}
	}

	/**
	 * 合并指定行的多个单元格
	 * 
	 * @param fromRow	开始行号
	 * @param column	要合并的列号
	 */
	public void autoMergeColumn(int fromRow, short column) 
	{
		HSSFCell cell = null;
		int from = 0;
		String preValue = "", currValue = "";
		lg.info("自动合并第" + column + "列（开始行数：" + fromRow + "）");
		for (int i = fromRow - 1; i < sheet.getPhysicalNumberOfRows(); i ++)
		{
			cell = sheet.getRow(i).getCell(column - 1);
			
			if (null == cell)
			{
				lg.warn("无效的单元格（" + (i + 1) + "，" + column + "）。");
				continue;
			}

//			lg.info("row:" + i + ",preValue:" + preValue + ",currValue:" + cell.getStringCellValue().trim());
			if (i == (fromRow - 1))	//第1行
			{
				currValue = cell.getRichStringCellValue().getString().trim();
				preValue = currValue;
				from = i;
				lg.debug("第1行");
			}
			else	//中间行
			{
				lg.debug("第" + (i + 1) + "行！");
				currValue = cell.getRichStringCellValue().getString().trim();
				if (currValue.equals(preValue))	//后移合并截至点
				{
//					lg.debug("相同-from:" + from + ",to:" + i);
				}
				else
				{
//					lg.debug("要合并了-from:" + from + ",to:" + (i - 1));
					//开始单元格合并
					if (i - from > 0)	//当前分组值只有1行数据，不合并
					{
						CellRangeAddress region = new CellRangeAddress(from, i - 1, column - 1, column - 1);
						sheet.addMergedRegion(region);
						//sheet.addMergedRegion(new Region(from, (short)(column - 1), i - 1, (short)(column - 1)));
					}
					
					//标志位后移。
					preValue = currValue;
					from = i;
				}
			}
		}

		//处理最后几行相同的情况。
		if (from != sheet.getPhysicalNumberOfRows() - 1)	//最后1段的相同情况没有处理
		{
			CellRangeAddress region = new CellRangeAddress(from, sheet.getPhysicalNumberOfRows() - 1, column - 1, column - 1);
			sheet.addMergedRegion(region);
			//sheet.addMergedRegion(new Region(from, (short)(column - 1), sheet.getPhysicalNumberOfRows() - 1, (short)(column - 1)));
		}
	}

	/**
	 * 自动合并指定列的单元格内容（根据名称相同原则）
	 * 
	 * @param fromRow			起始行号
	 * @param column			要合并的列号
	 * @param exampleColumn		合并样例列号
	 */
	public void autoMergeColumn(int fromRow, short column, short exampleColumn) 
	{
		try
		{
			int from = 0;
			String preValue = "", currValue = "";
			for (int i = fromRow - 1; i < sheet.getPhysicalNumberOfRows(); i ++)
			{
				//cell = sheet.getRow(i).getCell((short)(column - 1));
			
	//			lg.info("row:" + i + ",preValue:" + preValue + ",currValue:" + cell.getStringCellValue().trim());
				if (i == (fromRow - 1))	//第1行
				{
					currValue = getCellValue(i + 1, exampleColumn);
					preValue = currValue;
					from = i;
	//				lg.info("第1行");
				}
				else	//中间行
				{
					currValue = getCellValue(i + 1, exampleColumn);
					if (currValue.equals(preValue))	//后移合并截至点
					{
						lg.debug("相同-from:" + from + ",to:" + i);
					}
					else
					{
						lg.debug("要合并了-from:" + from + ",to:" + (i - 1));
						//开始单元格合并
						if (i - from > 0)	//当前分组值只有1行数据，不合并
							sheet.addMergedRegion(new CellRangeAddress(from, i - 1, (short)(column - 1), (short)(column - 1)));
						
						//标志位后移。
						preValue = currValue;
						from = i;
					}
				}
			}

			//处理最后几行相同的情况。
			if (from != sheet.getPhysicalNumberOfRows() - 1)	//最后1段的相同情况没有处理
				sheet.addMergedRegion(new CellRangeAddress(from, sheet.getPhysicalNumberOfRows() - 1, column - 1, column - 1));
		}
		catch (Exception e)
		{
			lg.error("自动合并单元格错误：", e);
		}
	}

	/**
	 * 获取字符串格式的单元格内容
	 * 
	 * @param row		行号
	 * @param column	列号
	 * 
	 * @return String（Null－如果单元格不存在）
	 */
	public String getCellValue(int row, short column)
	{
		String value = null;
		
		value = getCellContent(row, column);
			
		//检查是否是合并单元格，调整获取内容的行列位置
		if ("".equals(value))
		{
			CellRangeAddress region = null;
			for (int i = 0; i < sheet.getNumMergedRegions(); i++)
			{
				region = sheet.getMergedRegion(i);
				//区域范围判断
				if (region.getFirstColumn() <= (column - 1) && (column - 1) <= region.getLastColumn() &&
					region.getFirstRow() <= (row - 1) && (row - 1) <= region.getLastRow())
				{
					value = getCellContent(region.getFirstRow() + 1, (short) (region.getFirstColumn() + 1));
					break;
				}
			}
		}

		return value;
	}

	/**
	 * 获取单元格内容
	 * 
	 * @param row		行号
	 * @param column	列号
	 * 
	 * @return String（Null－如果单元格不存在）
	 */
	private String getCellContent(int row, int column)
	{
		HSSFCell cell = sheet.getRow(row - 1).getCell(column - 1);
		if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType())
			return NumberUtil.format(cell.getNumericCellValue());	//数字格式
		else
			return cell.getStringCellValue();	//字符串格式
	}

	/**
	 * 设置指定行的边框样式
	 * 
	 * @param row			行号
	 * @param position		边框位置，详见BORDER_###属性
	 * @param style			边框样式详见HSSFCellStyle.BORDER_###属性
	 */
	public void setBorderStyle(int rowNumber, int fromColum, int toColum, int position, int style)
	{
		HSSFRow row = sheet.getRow(rowNumber - 1);
//		int columNumber = row.getPhysicalNumberOfCells();

		HSSFCell cell = null;
		for (int i = fromColum - 1; i < toColum; i++)
		{
			cell = row.getCell(i);

			if (position == BORDER_TOP)
				cell.getCellStyle().setBorderTop((short)style);

			if (position == BORDER_BOTTOM)
				cell.getCellStyle().setBorderBottom((short)style);

			if (position == BORDER_LEFT)
				cell.getCellStyle().setBorderLeft((short)style);

			if (position == BORDER_RIGHT)
				cell.getCellStyle().setBorderRight((short)style);
		}
	}

	/**
	 * 设置指定单元格的背景颜色
	 * 
	 * @param row				行号
	 * 	@param row				列号
	 * @param color			颜色值（HSSFColor类静态变量）
	 */
	public void setCellBGColor(int row, int colum, short color)
	{
		HSSFCellStyle newCellStyle = workBook.createCellStyle();
		newCellStyle.cloneStyleFrom(sheet.getRow(row - 1).getCell(colum - 1).getCellStyle());
		newCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		newCellStyle.setFillForegroundColor(color);

		sheet.getRow(row - 1).getCell(colum - 1).setCellStyle(newCellStyle);
	}

	/**
	 * 给指定单元格添加图片内容
	 *
	 * @param fromRowNumber
	 * @param fromColNumber
	 * @param toRowNumber
	 * @param toColNumber
	 * 
	 * @throws IOException 
	 */
	public void addImage(int fromRowNumber, short fromColNumber, int toRowNumber, short toColNumber, String filePath)
		throws IOException
	{
		addImage(fromRowNumber, fromColNumber, 0, 0, toRowNumber, toColNumber, 1, 1, filePath);
	}

	/**
	 * 给指定单元格添加图片内容（指定左右上下边距百分比）
	 * 
	 * @param fromRowNumber			左上角单元格行号
	 * @param fromColNumber			左上角单元格列号
	 * @param dx1								左上角单元格左侧间距百分比
	 * @param dy1								左上角单元格顶部间距百分比
	 * @param toRowNumber				右下角单元格行号
	 * @param toColNumber				右下角单元格列号
	 * @param dx2								右下角单元格右侧间距百分比
	 * @param dy2								右下角单元格底部间距百分比
	 * @param filePath							图片文件绝对路径
	 * @throws IOException
	 */
	public void addImage(int fromRowNumber, short fromColNumber, float dx1, float dy1, int toRowNumber, short toColNumber, float dx2, float dy2, String filePath)
		throws IOException
	{
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray 
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(); 
		BufferedImage bufferImg = ImageIO.read(new File(filePath)); 
		ImageIO.write(bufferImg, "jpg", byteArrayOut);

		int index = workBook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
		HSSFClientAnchor anchor = new HSSFClientAnchor((int) (1023 * dx1), (int) (255 * dy1), (int) (1023 * dx2), (int) (255 * dy2),
				(short) (fromColNumber - 1), fromRowNumber - 1, (short) (toColNumber - 1), toRowNumber - 1);

		// 防护代码 - DrawingPatriarch对象有时候获取不到
		if (null == patriarch)
		{
			patriarch = sheet.getDrawingPatriarch();
			if (null == patriarch)
				patriarch = sheet.createDrawingPatriarch();
		}
		patriarch.createPicture(anchor, index);		//.resize(1)		// 按照原始比例显示，右下角坐标无效。
	}

	/**
	 * 自动根据页面高度调整行高度
	 * 
	 * @param pageHeight	页面高度
	 */
	public void autoAdjustRowHeight(short pageHeight)
	{
		HSSFRow row = null;
		int rowHeight = 0;
		List<String> rows = new ArrayList<String>();
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i ++)
		{
			row = sheet.getRow(i);
			if (null != row && row.getHeight() > 0)
			{
				rowHeight += row.getHeight();
				rows.add(String.valueOf(i));
			}
		}

		if (pageHeight * 20 > rowHeight)
		{
			int offset = pageHeight * 20 - rowHeight;
			//行高调整值
			int adjustRowHeight = offset / rows.size();

			for (String rowIndex : rows)
			{
				row = sheet.getRow(Short.valueOf(rowIndex));
				row.setHeight((short)(row.getHeight() + adjustRowHeight));
			}
		}
	}

	/**
	 * 内容保存至指定路径的文件中
	 */
	public void saveToFile(String filePath) throws Exception
	{
		if (null == filePath)
		{
			lg.warn("文件路径为Null，无法保存文件内容。");
			return;
		}
		else
		{
			java.io.File file = new java.io.File(filePath);	
			FileOutputStream fileOut = new FileOutputStream(file);
			workBook.write(fileOut);
			fileOut.close();
		}
	}
	
	/**
	 * 获取当前文档的输出流信息
	 * 
	 * @return
	 */
	public HSSFWorkbook getContent()
	{
		return workBook;
	}
	
	/**
	 * 拷贝特定区域单元格并粘贴到指定位置
	 * 
	 * @param srcTopLeft					左上角单元格编号
	 * @param srcBottomRight			右下角单元格编号
	 * @param destTopLeft				目标单元格左上角编号
	 */
	public void copyPasteCells(int fromRow, int fromColumn, int toRow, int toColumn, int destRow)
	{
		for (int i = fromRow; i < toRow; i++)
		{
			createRow(destRow + (i - fromRow), i, true);
		}
	}

	/**
	 * 删除行
	 * 
	 * @param fromRow	起始行
	 * @param rows			行总数
	 */
	public void deleteRows(int fromRow, int rows)
	{
		for (int i = 0; i < rows; i++)
		{
			sheet.removeRow(sheet.getRow(fromRow + i - 1));
		}
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
//		Excel excel = new Excel("E:/personal/java_project/JHZHH/web/WEB-INF/report/buy_contract.xls");
//		
//		excel.setCurrentSheet(1);
//		
//		//动态创建行，合并单元格测试代码
//		String[] excelData = new String[3];
//		excelData[0] = "1##系统管理##001001##/system/index.jsp##好东西";
//		excelData[1] = "2##Phone增值服务管理##001002##/valueAdded/index.jsp##实用";
//		excelData[2] = "3##CDR查询统计管理##001003##/cdrSearch/index.jsp##好用";
//		excel.insert(4, excelData);
//		
//		//新建一个合计行，并合并相关的单元格
//		excel.createRow(7, 6);
//		excel.mergeCell(7, 1, 7, 5);
//		excel.insert(7, 1, "这是一个合计行：1,231,252.00");
//		
//		excel.createRow(8, 4);
//		excelData[0] = "4##系统管理##001001##/system/index.jsp##好东西";
//		excelData[1] = "5##Phone增值服务管理##001002##/valueAdded/index.jsp##实用";
//		excelData[2] = "6##CDR查询统计管理##001003##/cdrSearch/index.jsp##好用";
//		excel.insert(8, excelData);
		
//		//动态创建列，合并单元格测试代码。
//		excel.createColumn((short)6, 5, (short)30);
//		//合并第一行
//		excel.mergeCell(1, 1, 1, 6);
//		//合并第二行
//		excel.mergeCell(2, 1, 2, 6);
//		//设置新增列的名称
//		excel.insert(3, 6, "新增列");
		
		//设置列宽度。
//		excel.sheet.setColumnWidth((short)1, (short)5000);
		
//		String[] data = new String[4];
//		data[0] = "陕西##SX##等等等等多大##23##45##5";
//		data[1] = "山西##SHX##eeeeeee##1##45##5";
//		data[2] = "河北##HB##刚发刚果河过多岁##3##45##5";
//		data[3] = "上海##ShH##算法三个发的撒阿瑟##5##45##5";
//		excel.insert(6, 1, data);
		
//		excel.setBorderStyle((short)4, Excel.BORDER_TOP, HSSFCellStyle.BORDER_MEDIUM);
//		excel.setBorderStyle((short)4, Excel.BORDER_BOTTOM, HSSFCellStyle.BORDER_MEDIUM);
//		excel.setBorderStyle((short)7, Excel.BORDER_BOTTOM, HSSFCellStyle.BORDER_MEDIUM);
		//excel.setBorderStyle((short)10, Excel.BORDER_BOTTOM, HSSFCellStyle.BORDER_MEDIUM);
//		
//		//文字测试
//		excel.insert(13, (short) 3, "嘉禾植化公司");
//		
//		//图片测试
//		excel.addImage(9, (short)9, 14, (short)11, "E:/personal/java_project/JHZHH/web/image/product/001001001003.jpg");
//
//		excel.setRowHeight(35, (short) 40);
//		excel.saveToFile("C:\\ReportTest.xls");
		
//		//gif格式测试通过
//		Excel excel = new Excel("C:/发货唛头模块.xls");
//		HSSFWorkbook book = excel.getContent();
//		
//		book.cloneSheet(0);
//		book.setSheetName(1, "1");
//		//插入图片
//		excel.setCurrentSheet(1);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//		
//		book.cloneSheet(1);
//		book.setSheetName(2, "2");
//		//插入图片
//		excel.setCurrentSheet(2);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//		
//		book.cloneSheet(2);
//		book.setSheetName(3, "3");
//		//插入图片
//		excel.setCurrentSheet(3);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//		
//		//要比sheet页多出一个sheet页，然后将模板sheet页和最后一个sheet页（图片不能正常显示）删除
//		book.cloneSheet(3);
//		book.setSheetName(4, "4");
//		//插入图片
//		excel.setCurrentSheet(4);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//
//		book.cloneSheet(4);
//		book.setSheetName(5, "5");
//		//插入图片
//		excel.setCurrentSheet(5);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//		
//		book.cloneSheet(5);
//		book.setSheetName(6, "6");
//		//插入图片
//		excel.setCurrentSheet(6);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");
//		
//		book.cloneSheet(6);
//		book.setSheetName(7, "7");
//		//插入图片
//		excel.setCurrentSheet(7);
//		excel.addImage(5, (short)5, 14, (short)6, "C:/flower.gif");

		//删除样式Sheet页和最后一个Sheet页。
//		book.removeSheetAt(4);
//		book.removeSheetAt(0);
//
//		Excel excel = new Excel("C:/Temp/pickup.xls");
//		excel.setCurrentSheet(1);
//
//		excel.copyRegion(4, 10, 5, 10, 10, false);
//		excel.saveToFile("C:/Temp/ReportTest.xls");
//
//		lg.info("文件保存成功！");
	}
}