package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Service;

@Component
@Transactional
public class ServiceToStringConverter implements
		Converter<Service, String> {

	@Override
	public String convert(final Service room) {
		String result;

		if (room == null)
			result = null;
		else
			result = String.valueOf(room.getId());
		return result;
	}
}
