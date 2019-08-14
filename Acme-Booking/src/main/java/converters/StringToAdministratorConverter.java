package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.AdministratorRepository;

import domain.Administrator;

public class StringToAdministratorConverter implements
		Converter<String, Administrator> {

	@Autowired
	AdministratorRepository adminRepository;

	@Override
	public Administrator convert(final String text) {
		Administrator result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.adminRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}