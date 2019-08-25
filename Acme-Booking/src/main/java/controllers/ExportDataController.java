package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.FinderService;
import services.RoomService;
import services.ServiceService;
import services.UtilityService;
import domain.Administrator;
import domain.Category;
import domain.Customer;
import domain.Owner;
import domain.Room;
import domain.Service;

@Controller
public class ExportDataController extends AbstractController {

	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ServiceService serviceService;
	
	@RequestMapping(value = "administrator/export.do", method = RequestMethod.GET)
	public @ResponseBody void downloadFileAdministrator(HttpServletResponse resp) {
		String downloadFileName = "dataUser";
		String res;

		Administrator actor = (Administrator) this.utilityService.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + actor.getName()
				+ " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n"
				+ " \r\n" + "Surname: "	+ actor.getSurname() + " \r\n"
				+ " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: "
				+ actor.getEmail() + " \r\n" + "Phone Number: "
				+ actor.getPhoneNumber() + " \r\n" + "Address: "
				+ actor.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		
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
		String res;

		Customer customer = (Customer) this.utilityService.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + customer.getName() 
				+ " \r\n" + "Middle Name: " + customer.getMiddleName() + " \r\n"
				+ " \r\n" + "Surname: "	+ customer.getSurname() + " \r\n"
				+ " \r\n" + "Photo: " + customer.getPhoto() + " \r\n" + "Email: "
				+ customer.getEmail() + " \r\n" + "Phone Number: "
				+ customer.getPhoneNumber() + " \r\n" + "Address: "
				+ customer.getAddress() + " \r\n" + " \r\n" + "\r\n"
				
				+ "Credit Card:" + "\r\n" + "Holder:"
				+ customer.getCreditCard().getHolder() + "\r\n" +
				"Make:" + customer.getCreditCard().getMake() + "\r\n" + "Number:"
				+ customer.getCreditCard().getNumber() + "\r\n"
				+ "Date expiration:"
				+ customer.getCreditCard().getExpirationMonth() + "/"
				+ customer.getCreditCard().getExpirationYear() + "\r\n" + "CVV:"
				+ customer.getCreditCard().getCVV();
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";
		
		res += "Finder:";
		res += "\r\n";
//		res += "Results in last search:" + "\r\n" 
//				+ this.finderService.findFinderByCustomerId(customer.getId()).getResults()
//				+ "\r\n\r\n";

		res += "\r\n\r\n";
		res += "----------------------------------------";

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
		String res;
		
		Owner owner = (Owner) this.utilityService.findByPrincipal();

		res = "Data of your user account:";
		res += "\r\n\r\n";
		res += "Name: " + owner.getName()
				+ " \r\n" + "Middle Name: " + owner.getMiddleName() + " \r\n"
				+ " \r\n" + "Surname: "	+ owner.getSurname() + " \r\n"
				+ " \r\n" + "Photo: " + owner.getPhoto() + " \r\n" + "Email: "
				+ owner.getEmail() + " \r\n" + "Phone Number: "
				+ owner.getPhoneNumber() + " \r\n" + "Address: "
				+ owner.getAddress() + " \r\n" + " \r\n" + "\r\n";
		
		res += "\r\n\r\n";
		res += "----------------------------------------";
		res += "\r\n\r\n";

		res += "Rooms::";
		res += "\r\n\r\n";
		Collection<Room> rooms = this.roomService.findRoomsMine(owner.getId());
		for (Room room : rooms) {
			res += "Room: " + "\r\n\r\n";
			res += "Title: " + room.getTitle()+ "\r\n\r\n";
			res += "Ticker: " + room.getTicker()+ "\r\n\r\n";
			res += "Status: " + room.getStatus()+ "\r\n\r\n";
			res += "Addres: " + room.getAddress()+ "\r\n\r\n";
			res += "Schedule details: " + room.getScheduleDetails()+ "\r\n\r\n";
			res += "Opening hour: " + room.getOpeningHour()+ "\r\n\r\n";
			res += "Closing hour" + room.getClosingHour()+ "\r\n\r\n";
			res += "Prove of ownership: " + room.getProveOfOwnership()+ "\r\n\r\n";
			res += "Photos: " + room.getPhotos()+ "\r\n\r\n";
			res += "Capacity: " + room.getCapacity()+ "\r\n\r\n";
			res += "Administrator: " + room.getAdministrator()+ "\r\n\r\n";
			res += "Categories: ";
			
			Collection<Category> categoriesOfRoom = room.getCategories();
			res+= " -- ";
			for(Category category : categoriesOfRoom) {
				res+= category.getTitle().get("English");
				res+= " -- ";
			}
			res+= "\r\n\r\n\r\n\r\n";
			
			res += "Services: " + "\r\n\r\n";
			
			Collection<Service> servicesOfRoom = this.serviceService.findServicesByRoomId(room.getId());
			
			for(Service service : servicesOfRoom) {
				res += "Service: " + "\r\n\r\n";
				res += "Name: " + service.getName()+ "\r\n\r\n";
				res += "Description: " + service.getDescription()+ "\r\n\r\n";
				res += "Price: " + service.getPrice()+ "\r\n\r\n";
				
				res+= "\r\n\r\n";
				res += "------";
				res+= "\r\n\r\n";
			}
			res+= "\r\n\r\n";
			res += "-----------";
			res+= "\r\n\r\n";
			
		}
		
		res += "\r\n\r\n";
		res += "----------------------------------------";

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
