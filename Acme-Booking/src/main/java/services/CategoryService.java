package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Actor;
import domain.Category;
import domain.Room;
import domain.SystemConfiguration;

@Service
@Transactional
public class CategoryService {

	// Managed repository ------------------------------
	
	@Autowired
	private CategoryRepository categoryRepository;

	// Supporting services -----------------------

	@Autowired
	private UtilityService	utilityService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private Validator validator;

	// CRUD methods -----------------------------------

	public Category create() {
		Category result;
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");

		result = new Category();
		result.setTitle(new HashMap<String,String>());
		result.setChildCategories(new ArrayList<Category>());
		result.setRooms(new ArrayList<Room>());
		
		return result;
	}
	
	public Category findOne(final int categoryId) {
		return this.categoryRepository.findOne(categoryId);
	}

	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}
	
	public Category save(final Category category) {
		Category result,aux;
		SystemConfiguration systemConf;
		Set<String> idiomasCategory;
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");
		
		Assert.notNull(category.getChildCategories());
		Assert.notNull(category.getRooms());
		Assert.notNull(category.getTitle());
		
		systemConf = systemConfigurationService.findMySystemConfiguration();
		Set<String> idiomasSystemConf = new HashSet<String>(systemConf
				.getWelcomeMessage().keySet());
		idiomasCategory = category.getTitle().keySet();
		Assert.isTrue(idiomasSystemConf.equals(idiomasCategory));
		
		if(category.getTitle().containsValue("CONFERENCE")) {
			aux = this.findOne(category.getId());
			category.setParentCategory(aux.getParentCategory());
			category.setTitle(aux.getTitle());
		} else {
			Assert.notNull(category.getParentCategory());
		}
		
		result = this.categoryRepository.save(category);
		Assert.notNull(result);
		
		if(category.getId() == 0) {
			this.addNewChild(result);
		}
		
		return result;
	}

	public void delete(final Category category) {
		Actor principal;
		
		principal = this.utilityService.findByPrincipal();
		Assert.isTrue(
				this.utilityService.checkAuthority(principal, "ADMIN"),
				"not.allowed");
		
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);
		
		this.deleteFromParent(category);
		
		if(!category.getChildCategories().isEmpty()) {
			this.transferChildren(category);
		}
		
		if(!category.getRooms().isEmpty()) {
			this.transferRooms(category);
		}
		
		this.categoryRepository.delete(category);
	}

	// Other business methods -------------------------------
	
	public Category reconstruct(Category category, String nameES,
			String nameEN, BindingResult binding) {
		Category res = this.create();
		Map<String,String> aux = new HashMap<String,String>();
		
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal,
				"ADMIN"));
		try {
			Assert.isTrue(!nameEN.isEmpty(), "no.both.names");
			Assert.isTrue(!nameES.isEmpty(), "no.both.names");
		} catch (Throwable oops) {
			binding.rejectValue("title", "no.both.names");
		}
		
		aux.put("Español", nameES);
		aux.put("English", nameEN);
		
		if (category.getId() != 0) {
			res = this.categoryRepository.findOne(category.getId());
		} else {
			res.setParentCategory(category.getParentCategory());
		}
		res.setTitle(aux);

		this.validator.validate(res, binding);

		return res;
	}
	
	private void transferRooms(Category category) {
		Category parent = category.getParentCategory();
		Collection<Room> roomsToTransfer = category.getRooms();
		Collection<Room> rooms = parent.getRooms();
		
		rooms.addAll(roomsToTransfer);
		parent.setRooms(rooms);
		
		for(Room room : roomsToTransfer) {
			room.setCategory(parent);
			this.roomService.save(room);
		}
		this.save(parent);
	}
	
	private void transferChildren(Category category) {
		Category parent = category.getParentCategory();
		Collection<Category> childrenToTransfer = category.getChildCategories();
		Collection<Category> children = parent.getChildCategories();
		
		children.addAll(childrenToTransfer);
		parent.setChildCategories(children);
		
		for(Category cat : childrenToTransfer) {
			cat.setParentCategory(parent);
			this.save(cat);
			//this.addNewChild(cat);
		}
		this.save(parent);
	}
	
	private void deleteFromParent(Category category) {
		Category parent = category.getParentCategory();
		Collection<Category> aux = parent.getChildCategories();
		aux.remove(category);
		parent.setChildCategories(aux);
		
		this.save(parent);
	}
	
	private void addNewChild(Category category) {
		Collection<Category> children;
		Category parent = this.findOne(category.getParentCategory().getId());
		
		children = parent.getChildCategories();
		children.add(category);
		parent.setChildCategories(children);
		this.save(parent);
	}
	
	public Collection<Category> parseCategories (String [] array) {
		Collection<Category> result = new ArrayList<>();
		String a = null;
		Integer n = 0;
		Category pos = null;
		
		for (int i = 0; i <= array.length - 1; i++) {
			a = array[i];
			n = Integer.parseInt(a);
			pos = this.findOne(n);
			result.add(pos);
		}
		this.flush();
		return result;
	}
	
	public void flush(){
		this.categoryRepository.flush();
	}
	
	public Category findOneByRoomId(int roomId) {
		
		return this.categoryRepository.findOneByRoomId(roomId);
	}
	
	public void addNewRoom(Collection<Category> categories, Room room) {
		for(Category cat : categories) {
			Collection<Room> collCon = cat.getRooms();
			collCon.add(room);
			cat.setRooms(collCon);
		}
	}
	
	public Collection<Category> findAllAsAdmin() {
		Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal,
				"ADMIN"));

		return this.categoryRepository.findAll();
	}
}

