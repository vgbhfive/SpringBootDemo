package cn.vgbhfive.poidemo.controller;

import cn.vgbhfive.poidemo.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @time: 2019/10/13
 * @author: Vgbh
 */
@RestController
@RequestMapping("/export")
public class ExportDemoController {

	@RequestMapping(value = "/demo1", method = RequestMethod.GET)
	public void exportDemoExcel(HttpServletResponse response) {
		// 表头
		List<String> headers = new ArrayList<>();
		headers.add("test1");
		headers.add("test2");
		headers.add("test3");
		headers.add("test4");

		// 数据
		List<List<String>> data = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			List<String> list = new ArrayList<>();
			list.add((i+1) + "-1");
			list.add((i+1) + "-2");
			list.add((i+1) + "-3");
			list.add((i+1) + "-4");
			data.add(list);
		}

		// 导出
		String fileName = "test.xls";
		HSSFWorkbook hssfWorkbook = ExcelUtils.expExcel(headers, data);
		ExcelUtils.outFile(hssfWorkbook, "./"+fileName, response);
	}

}
