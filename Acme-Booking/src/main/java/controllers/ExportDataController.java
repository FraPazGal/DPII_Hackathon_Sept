//package controllers;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.charset.Charset;
//import java.util.Collection;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import services.CommentService;
//import services.FinderService;
//import services.IRobotService;
//import services.PurchaseService;
//import services.UtilityService;
//import domain.Administrator;
//import domain.Material;
//import domain.Customer;
//import domain.Room;
//import domain.Purchase;
//import domain.Owner;
//
//@Controller
//public class ExportDataController extends AbstractController {
//
//	@Autowired
//	private UtilityService utilityService;
//	
//	@Autowired
//	private FinderService finderService;
//	
//	@Autowired
//	private PurchaseService purchaseService;
//	
//	@Autowired
//	private CommentService commentService;
//	
//	@Autowired
//	private IRobotService iRobotService;
//
//	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
//	public @ResponseBody void downloadFileAdministrator(HttpServletResponse resp) {
//		String downloadFileName = "dataUser";
//		String res;
//
//		Administrator actor = (Administrator) this.utilityService.findByPrincipal();
//
//		res = "Data of your user account:";
//		res += "\r\n\r\n";
//		res += "Name: " + actor.getName() + " \r\n" + "Surname: "
//				+ actor.getSurname() + " \r\n" 
//				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
//				+ actor.getEmail() + " \r\n" + "Phone Number: "
//				+ actor.getPhoneNumber() + " \r\n" + "Address: "
//				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//		
//		res += "Comments:";
//		res += "\r\n\r\n";
//		Collection<Material> materials = this.commentService.findCommentByActorId(actor.getId());
//		for (Material material : materials) {
//			res += "Comment: " + "\r\n\r\n";
//			res += "Published Date: " + material.getPublishedDate()+ "\r\n\r\n";
//			res += "Title: " + material.getTitle()+ "\r\n\r\n";
//			res += "Body: " + material.getBody()+ "\r\n\r\n";
//			res += "iRobot: " + material.getIRobot().getTitle()+ "\r\n\r\n";
//			res += "-----------";
//			res += "\r\n\r\n";
//		}
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		
//		String downloadStringContent = res; // implement this
//		try {
//			OutputStream out = resp.getOutputStream();
//			resp.setContentType("text/html; charset=utf-8");
//			resp.addHeader("Content-Disposition", "attachment; filename=\""
//					+ downloadFileName + "\"");
//			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//		}
//	}
//
//	@RequestMapping(value = "customer/export.do", method = RequestMethod.GET)
//	public @ResponseBody void downloadFileRookie(HttpServletResponse resp) {
//		String downloadFileName = "dataUser";
//		String res;
//
//		Customer customer = (Customer) this.utilityService.findUserByPrincipal();
//
//		res = "Data of your user account:";
//		res += "\r\n\r\n";
//		res += "Name: " + customer.getName() + " \r\n" + "Surname: "
//				+ customer.getSurname() + " \r\n" + "VAT:" + customer.getVATNumber()
//				+ " \r\n" + "Photo: " + customer.getPhoto() + " \r\n" + "Email: "
//				+ customer.getEmail() + " \r\n" + "Phone Number: "
//				+ customer.getPhoneNumber() + " \r\n" + "Address: "
//				+ customer.getAddress() + " \r\n" + " \r\n" + "\r\n"
//				
//				+ "Credit Card:" + "\r\n" + "Holder:"
//				+ customer.getCreditCard().getHolder() + "\r\n" +
//				"Make:" + customer.getCreditCard().getMake() + "\r\n" + "Number:"
//				+ customer.getCreditCard().getNumber() + "\r\n"
//				+ "Date expiration:"
//				+ customer.getCreditCard().getExpirationMonth() + "/"
//				+ customer.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
//				+ customer.getCreditCard().getCVV();
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//		
//		res += "Finder:";
//		res += "\r\n";
//		res += "Results in last search:" + "\r\n" 
//				+ this.finderService.findFinderByCustomerId(customer.getId()).getResults()
//				+ "\r\n\r\n";
//
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//
//		res += "Purchases:";
//		res += "\r\n\r\n";
//		Collection<Purchase> purchases = this.purchaseService.purchasesPerCustomer(customer.getId());
//		for (Purchase purchase : purchases) {
//			res += "Purchase: " + "\r\n\r\n";
//			res += "Purchase moment: " + purchase.getPurchaseMoment()+ "\r\n\r\n";
//			res += "iRobot: " + purchase.getiRobot().getTitle() + "\r\n\r\n"
//			+ "Credit Card:" + "\r\n" + "Holder:"
//			+ purchase.getCreditCard().getHolder() + "\r\n" +
//			"Make:" + purchase.getCreditCard().getMake() + "\r\n" + "Number:"
//			+ purchase.getCreditCard().getNumber() + "\r\n"
//			+ "Date expiration:"
//			+ purchase.getCreditCard().getExpirationMonth() + "/"
//			+ purchase.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
//			+ purchase.getCreditCard().getCVV();
//			res += "-----------";
//			res += "\r\n\r\n";
//		}
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//		
//		res += "Comments:";
//		res += "\r\n\r\n";
//		Collection<Material> materials = this.commentService.findCommentByActorId(customer.getId());
//		for (Material material : materials) {
//			res += "Comment: " + "\r\n\r\n";
//			res += "Published Date: " + material.getPublishedDate()+ "\r\n\r\n";
//			res += "Title: " + material.getTitle()+ "\r\n\r\n";
//			res += "Body: " + material.getBody()+ "\r\n\r\n";
//			res += "iRobot: " + material.getIRobot().getTitle()+ "\r\n\r\n";
//			res += "-----------";
//			res += "\r\n\r\n";
//		}
//
//		String downloadStringContent = res;
//		try {
//			OutputStream out = resp.getOutputStream();
//			resp.setContentType("text/html; charset=utf-8");
//			resp.addHeader("Content-Disposition", "attachment; filename=\""
//					+ downloadFileName + "\"");
//			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//		}
//	}
//
//	@RequestMapping(value = "scientist/export.do", method = RequestMethod.GET)
//	public @ResponseBody
//	void downloadFileCompany(HttpServletResponse resp) {
//		String downloadFileName = "dataUser";
//		String res;
//		
//		Owner owner = (Owner) this.utilityService.findUserByPrincipal();
//
//		res = "Data of your user account:";
//		res += "\r\n\r\n";
//		res += "Name: " + owner.getName() + " \r\n" + "Surname: "
//				+ owner.getSurname() + " \r\n" + "VAT:" + owner.getVATNumber()
//				+ " \r\n" + "Photo: " + owner.getPhoto() + " \r\n" + "Email: "
//				+ owner.getEmail() + " \r\n" + "Phone Number: "
//				+ owner.getPhoneNumber() + " \r\n" + "Address: "
//				+ owner.getAddress() + " \r\n" + " \r\n" + "\r\n"
//				
//				+ "Credit Card:" + "\r\n" + "Holder:"
//				+ owner.getCreditCard().getHolder() + "\r\n" +
//				"Make:" + owner.getCreditCard().getMake() + "\r\n" + "Number:"
//				+ owner.getCreditCard().getNumber() + "\r\n"
//				+ "Date expiration:"
//				+ owner.getCreditCard().getExpirationMonth() + "/"
//				+ owner.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
//				+ owner.getCreditCard().getCVV();
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//
//		res += "iRobots:";
//		res += "\r\n\r\n";
//		Collection<Room> rooms = this.iRobotService.findIRobotsMine(owner.getId());
//		for (Room room : rooms) {
//			res += "iRobot: " + "\r\n\r\n";
//			res += "Title: " + room.getTitle()+ "\r\n\r\n";
//			res += "Ticker: " + room.getTicker()+ "\r\n\r\n";
//			res += "Description: " + room.getDescription()+ "\r\n\r\n";
//			res += "Price: " + room.getPrice()+ "\r\n\r\n";
//			res += "Decomissioned: " + room.getIsDecomissioned()+ "\r\n\r\n";
//			res += "Deleted: " + room.getIsDeleted()+ "\r\n\r\n";
//			res += "-----------";
//			res += "\r\n\r\n";
//		}
//		
//		res += "\r\n\r\n";
//		res += "----------------------------------------";
//		res += "\r\n\r\n";
//		
//		res += "Comments:";
//		res += "\r\n\r\n";
//		Collection<Material> materials = this.commentService.findCommentByActorId(owner.getId());
//		for (Material material : materials) {
//			res += "Comment: " + "\r\n\r\n";
//			res += "Published Date: " + material.getPublishedDate()+ "\r\n\r\n";
//			res += "Title: " + material.getTitle()+ "\r\n\r\n";
//			res += "Body: " + material.getBody()+ "\r\n\r\n";
//			res += "iRobot: " + material.getIRobot().getTitle()+ "\r\n\r\n";
//			res += "-----------";
//			res += "\r\n\r\n";
//		}
//
//		String downloadStringContent = res;
//		try {
//			OutputStream out = resp.getOutputStream();
//			resp.setContentType("text/html; charset=utf-8");
//			resp.addHeader("Content-Disposition", "attachment; filename=\""
//					+ downloadFileName + "\"");
//			out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//		}
//	}
//}
