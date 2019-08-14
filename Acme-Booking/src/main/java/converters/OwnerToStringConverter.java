package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Owner;

@Component
@Transactional
public class OwnerToStringConverter implements Converter<Owner, String> {

	@Override
	public String convert(final Owner owner) {
		String result;

		if (owner == null)
			result = null;
		else
			result = String.valueOf(owner.getId());
		return result;
	}
}
