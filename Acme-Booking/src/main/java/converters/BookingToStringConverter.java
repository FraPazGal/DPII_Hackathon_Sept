
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Booking;

@Component
@Transactional
public class BookingToStringConverter implements Converter<Booking, String> {

	@Override
	public String convert(final Booking room) {
		String result;

		if (room == null)
			result = null;
		else
			result = String.valueOf(room.getId());
		return result;
	}
}
