package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Request;

@Component
@Transactional
public class RequestToStringConverter implements
		Converter<Request, String> {

	@Override
	public String convert(final Request room) {
		String result;

		if (room == null)
			result = null;
		else
			result = String.valueOf(room.getId());
		return result;
	}
}
