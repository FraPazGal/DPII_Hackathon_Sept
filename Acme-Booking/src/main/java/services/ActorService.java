package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import domain.Actor;

@Transactional
@Service
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private UtilityService	utilityService;

	/**
	 * Get all actors from database
	 * 
	 * @return Collection<Actor>
	 */
	public Collection<Actor> findAll() {
		Collection<Actor> result = null;
		result = this.actorRepository.findAll();
		return result;
	}

	/**
	 * Find an actor by id in the database
	 * 
	 * @param actorId
	 * @return actor
	 */
	public Actor findOne(final int actorId) {
		final Actor result = this.actorRepository.findOne(actorId);
		Assert.notNull(result, "wrong.id");
		return result;
	}

	public boolean existsUsername(String username) {
		return !(this.actorRepository.existsUsername(username) != null);
	}
	
	public void ban(Integer actorId) {
		Actor toBan = this.findOne(actorId);
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		Assert.isTrue(!toBan.getUserAccount().getIsBanned(), "wrong.id");
		
		if(toBan.getIsSpammer() != null) {
			Assert.isTrue(toBan.getIsSpammer(),"not.spammer");
		} else {
			Assert.notNull(toBan.getIsSpammer(), "not.spammer");
		}
		toBan.getUserAccount().setIsBanned(true);
		toBan = this.actorRepository.save(toBan);
	}

	public void unban(Integer actorId) {
		Actor toUnban = this.findOne(actorId);
		Actor principal = this.utilityService.findByPrincipal();
		
		Assert.isTrue(this.utilityService.checkAuthority(principal, "ADMIN"), "not.allowed");
		Assert.isTrue(toUnban.getUserAccount().getIsBanned(), "wrong.id");
		
		toUnban.getUserAccount().setIsBanned(false);
		toUnban = this.actorRepository.save(toUnban);
	}
	
	public Collection<Actor> findAllExceptPrincipal() {
		Collection<Actor> result = this.actorRepository.findAll();
		Actor principal = this.utilityService.findByPrincipal();
		
		result.remove(principal);
		
		return result;
	}
}
