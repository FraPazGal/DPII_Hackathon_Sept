package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.AdministratorService;
import services.CustomerService;
import services.OwnerService;

@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OwnerService ownerService;
	
	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody void downloadFileAdministrator(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res = this.administratorService.exportData();

		String downloadStringContent = res; // implement this
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "customer/export.do", method = RequestMethod.GET)
	public @ResponseBody void downloadFileRookie(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res = this.customerService.exportData();

		String downloadStringContent = res;
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "owner/export.do", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFileCompany(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res = this.ownerService.exportData();
		
		String downloadStringContent = res;
		try {
			OutputStream out = resp.getOutputStream();
			resp.setContentType("text/html; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFileName + "\"");
			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}
}
