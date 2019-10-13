package cn.vgbhfive.poidemo.utils;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Excel 工具类
 *
 * @time: 2019/10/13
 * @author: Vgbh
 */
@Service
public class ExcelUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

	private static final short borderRpx = 1;

	/**
	 * 导出Excel
	 * @param headers
	 * @param data
	 * @return
	 */
	public static HSSFWorkbook expExcel(List<String> headers, List<List<String>> data) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = null;
		HSSFCellStyle style = workbook.createCellStyle();
		setBorderStyle(style, borderRpx);
		style.setFont(setFontStyle(workbook, "黑体", (short) 14));
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		sheet.createFreezePane(0, 1, 0, 1);

		for (int i = 0; i < headers.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(style);
		}

		HSSFCellStyle style1 = workbook.createCellStyle();
		setBorderStyle(style1, borderRpx);
		style1.setFont(setFontStyle(workbook, "宋体", (short) 12));
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(i+1);
			List<String> param = data.get(i);
			for (int j = 0; j < param.size(); j++) {
				cell = row.createCell(j);
				cell.setCellValue(param.get(j));
				cell.setCellStyle(style1);
			}
		}

		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;
	}

	/**
	 * 导出文件
	 * @param workbook
	 * @param path
	 * @param response
	 */
	public static void outFile(HSSFWorkbook workbook, String path, HttpServletResponse response) {
		OutputStream os=null;
		File file = null;
		try {
			file = new File(path);
			String filename = file.getName();
			os = new FileOutputStream(file);
			response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));
			os= new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			workbook.write(os);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os.flush();
			os.close();
			System.gc();
			System.out.println(file.delete());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置字体样式
	 * @param workbook
	 * @param name
	 * @param height
	 * @return
	 */
	public static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(height);
		font.setFontName(name);
		return font;
	}

	/**
	 * 设置单元格样式
	 * @param cellStyle
	 * @param border
	 */
	public static void setBorderStyle(HSSFCellStyle cellStyle, short border) {
		cellStyle.setBorderBottom(border); // 下边框
		cellStyle.setBorderLeft(border);// 左边框
		cellStyle.setBorderTop(border);// 上边框
		cellStyle.setBorderRight(border);// 右边框
	}

}
