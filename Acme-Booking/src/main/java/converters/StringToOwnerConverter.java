package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.OwnerRepository;
import domain.Owner;

public class StringToOwnerConverter implements Converter<String, Owner> {

	@Autowired
	OwnerRepository repository;

	@Override
	public Owner convert(final String text) {
		Owner result;

		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}