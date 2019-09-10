
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 65'1
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 16.418
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 5.5%
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 927
	 * 
	 * ################################################################
	 * 
	 * Test 1: An admin creates a category
	 * 
	 * Test 2: An admin lists and edits a category
	 * 
	 * Test 3: An admin deletes a category
	 * 
	 */
	
	@Autowired
	private CategoryService	categoryService;
	
	
	//Test 1: An admin creates a category
	//Req. 12.2
	@Test
	public void driverCreateCategory() {
		final Object testingData[][] = {

			{
				"owner1", "Title_ES", "Title_EN", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to create a category

			{
				"customer1", "Title_ES", "Title_EN", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to create a category
			
			{
				"admin", "Title_ES", "", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to create a category with blank title
			
			{
				"admin", "Title_ES", "Title_EN", "Cinema", null
			},
			//Positive test case, an admin creates a category
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void template(final String user, final String title_ES, final String title_EN, final String parentEnglishTitle, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			Category parentCategory = this.findCategoryByTitle(parentEnglishTitle);
			this.createCategory(title_ES, title_EN, parentCategory);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public void createCategory(final String title_ES, final String title_EN, final Category parentCategory) {
		
		Category category = this.categoryService.create();
		
		category.setParentCategory(parentCategory);
		final BindingResult binding = new BeanPropertyBindingResult(category, category.getClass().getName());
		final Category result = this.categoryService.reconstruct(category, title_ES, title_EN, binding);
		Assert.isTrue(!binding.hasErrors());
		this.categoryService.save(result);
	}
	
	
	//Test 2: An admin lists and edits a category
	//Req. 12.2
	@Test
	public void driverEditCategory() {
		final Object testingData[][] = {

			{
				"owner1", "Title_ES", "Title_EN", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to edit a category

			{
				"customer1", "Title_ES", "Title_EN", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to edit a category
			
			{
				"admin", "Title_ES", "", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an admin tries to edit a category with blank title
			
			{
				"admin", "Title_ES", "Title_EN", "Cinema", null
			},
			//Positive test case, an admin edits a category
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void template2(final String user, final String title_ES, final String title_EN, 
			final String titleENCategoryToEdit, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			Category categoryToEdit = this.findCategoryByTitle(titleENCategoryToEdit);
			
			final BindingResult binding = new BeanPropertyBindingResult(categoryToEdit, categoryToEdit.getClass().getName());
			Category result = this.categoryService.reconstruct(categoryToEdit, title_ES, title_EN, binding);
			Assert.isTrue(!binding.hasErrors());
			this.categoryService.save(result);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}
	
	//Test 3: An admin deletes a category
	//Req. 12.2
	@Test
	public void driverDeleteCategory() {
		final Object testingData[][] = {

			{
				"owner1", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, an owner tries to delete a category

			{
				"customer1", "Cinema", IllegalArgumentException.class
			},
			//Negative test case, a customer tries to delete a category
			
			{
				"admin", "CATEGORY", IllegalArgumentException.class
			},
			//Negative test case,  an admin tries to delete the root category
			
			{
				"admin", "Cinema", null
			},
			//Positive test case, an admin deletes a category
		};

		for (int i = 0; i < testingData.length; i++) {
			this.template3((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void template3(final String user, final String titleENCategoryToEdit, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(user);
			Category categoryToDelete = this.findCategoryByTitle(titleENCategoryToEdit);
			this.categoryService.delete(categoryToDelete);
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		super.checkExceptions(expected, caught);
	}

	public Category findCategoryByTitle(String parentEnglishTitle) {
		Collection<Category> categories = this.categoryService.findAllAsAdmin();
		Category result = new Category();
		for(Category c : categories) {
			if(c.getTitle().containsValue(parentEnglishTitle)) {
				result = c;
				break;
			}
		}
		return result;
	}
}
