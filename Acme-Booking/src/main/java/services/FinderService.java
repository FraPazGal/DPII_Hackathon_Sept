
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Category;
import domain.Customer;
import domain.Finder;
import domain.Room;

@Transactional
@Service
public class FinderService {

	// Managed repository ------------------------------
	@Autowired
	private FinderRepository			finderRepository;

	// Supporting services -----------------------
	@Autowired
	private UtilityService				utilityService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private Validator					validator;


	// Constructors
	public FinderService() {
		super();
	}

	public Finder create() {
		Finder result;
		result = new Finder();
		final Collection<Room> rooms = new ArrayList<Room>();
		result.setResults(rooms);
		return result;
	}

	public Finder defaultFinder(final Customer customer) {
		Finder finder = this.finderRepository.findByCustomer(customer.getId());
		if (finder == null) {
			final Finder f = this.create();
			f.setCustomer(customer);
			finder = this.save(f);
		}
		return finder;
	}

	// /FINDONE
	public Finder findOne(final int finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);

		return result;
	}

	// FINDALL
	public Collection<Finder> findAll() {
		Collection<Finder> result;
		result = this.finderRepository.findAll();

		return result;

	}

	public Finder save(final Finder finder) {
		Finder result;
		Customer principal;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);
		if (finder.getId() != 0) {
			final Actor actor = this.utilityService.findByPrincipal();
			principal = (Customer) actor;
			Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
			Assert.isTrue(this.finderRepository.findByCustomer(principal.getId()).equals(finder), "not.allowed");
			Assert.notNull(finder, "not.allowed");
			if (finder.getMaximumFee() != null)
				Assert.isTrue(finder.getMaximumFee() >= 0., "not.negative");
			if (finder.getCapacity() != null)
				Assert.isTrue(finder.getCapacity() >= 0., "not.negative");
			finder.setSearchMoment(currentMoment);
		}
		result = this.finderRepository.save(finder);
		Assert.notNull(result, "not.null");
		return result;
	}

	//DELETE 
	public void delete(final Finder finder) {
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.findByCustomer(principal.getId()).getId() == (finder.getId()), "not.allowed");
		finder.setKeyWord(null);
		finder.setKeyWord(null);
		finder.setCategory(null);
		finder.setMaximumFee(null);
		finder.setCapacity(null);
		finder.setService(null);
		finder.setSearchMoment(null);
		final Collection<Room> rooms = new ArrayList<Room>();
		finder.setResults(rooms);
		this.finderRepository.save(finder);
	}

	public void deleteExpiredFinder(final Finder finder) {
		Date maxLivedMoment = new Date();
		int timeChachedFind;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);
		timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
		maxLivedMoment = DateUtils.addHours(currentMoment, -timeChachedFind);
		if (finder.getSearchMoment().before(maxLivedMoment)) {
			finder.setKeyWord(null);
			finder.setMaximumFee(null);
			finder.setCategory(null);
			finder.setCapacity(null);
			finder.setResults(null);
			finder.setService(null);
			finder.setSearchMoment(null);
			this.finderRepository.save(finder);
		}
	}
	public Finder search(final Finder finder) {
		Collection<Room> results = new ArrayList<Room>();
		String keyWord;
		String service;
		Double maximumFee;
		Integer capacity;
		String category;
		int nResults;
		if (finder.getMaximumFee() != null)
			Assert.isTrue(finder.getMaximumFee() >= 0., "not.negative");
		if (finder.getCapacity() != null)
			Assert.isTrue(finder.getCapacity() >= 0., "not.negative");
		final Collection<Room> resultsPageables = new ArrayList<Room>();
		nResults = this.systemConfigurationService.findMySystemConfiguration().getMaxResults();
		keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? "" : finder.getKeyWord();
		service = (finder.getService() == null || finder.getService().isEmpty()) ? "" : finder.getService();
		category = (finder.getCategory() == null || finder.getCategory().isEmpty()) ? "" : finder.getCategory();
		capacity = (finder.getCapacity() == null) ? 0 : finder.getCapacity();
		maximumFee = (finder.getMaximumFee() == null) ? 1000000000.0 : finder.getMaximumFee();
		if (service == "")
			results = this.finderRepository.search(keyWord, maximumFee, capacity);
		else
			results = this.finderRepository.searchByService(keyWord, maximumFee, capacity, service);
		if (category != "") {
			final Collection<Room> res = new ArrayList<Room>();
			for (final Room r : results)
				if (!r.getCategories().isEmpty())
					for (final Category c : r.getCategories())
						if (c.getTitle().get("Español").equals(category) || c.getTitle().get("English").equals(category))
							res.add(r);
			results = res;
		}
		int count = 0;
		for (final Room p : results) {
			resultsPageables.add(p);
			count++;
			if (count >= nResults)
				break;
		}
		finder.setResults(resultsPageables);
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);
		finder.setSearchMoment(currentMoment);
		//this.finderRepository.save(finder);
		return finder;
	}

	public Collection<Room> searchAnon(String keyWord) {

		Collection<Room> results;

		keyWord = (keyWord == null || keyWord.isEmpty()) ? "" : keyWord;

		results = this.finderRepository.searchAnon(keyWord);

		return results;
	}

	public void flush() {
		this.finderRepository.flush();
	}

	protected void deleteFinder(final Customer customer) {
		final Actor principal = this.utilityService.findByPrincipal();

		this.finderRepository.delete(this.finderRepository.findByCustomer(principal.getId()));
	}

	public Finder finderByCustomer() {
		Finder finder;
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		finder = this.finderRepository.findByCustomer(principal.getId());
		Date maxLivedMoment = new Date();
		if (finder.getSearchMoment() != null) {
			final int timeChachedFind = this.systemConfigurationService.findMySystemConfiguration().getTimeResultsCached();
			maxLivedMoment = DateUtils.addHours(maxLivedMoment, -timeChachedFind);
			if (finder.getSearchMoment().before(maxLivedMoment))
				this.deleteExpiredFinder(finder);
		}
		return finder;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		final Finder find = this.create();
		final Actor principal = this.utilityService.findByPrincipal();
		Assert.isTrue(this.utilityService.checkAuthority(principal, "CUSTOMER"), "not.allowed");
		final Finder orig = this.finderByCustomer();
		find.setId(orig.getId());
		find.setVersion(orig.getVersion());
		find.setCustomer(orig.getCustomer());
		find.setKeyWord(finder.getKeyWord());
		find.setMaximumFee(finder.getMaximumFee());
		find.setCategory(finder.getCategory());
		find.setCapacity(finder.getCapacity());
		find.setService(finder.getService());
		this.validator.validate(find, binding);
		return find;
	}

	public void deleteAccountOwner(final Room r) {
		final Collection<Finder> f = this.finderRepository.findByRoom(r.getId());
		if (!f.isEmpty())
			for (final Finder fi : f) {
				fi.getResults().remove(r);
				this.finderRepository.save(fi);
			}
	}

	public void deleteAccountCustomer() {
		final Actor principal = this.utilityService.findByPrincipal();
		final Finder f = this.finderRepository.findByCustomer(principal.getId());
		final Collection<Room> rooms = new ArrayList<Room>();
		f.setResults(rooms);
		this.finderRepository.save(f);
		this.finderRepository.delete(f);
	}
	
	public Finder findFinderByCustomerId(int customerId) {
		
		return this.finderRepository.findFinderByCustomerId(customerId);
	}
}
