
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import repositories.BookingRepository;
import domain.Booking;

public class StringToBookingConverter implements Converter<String, Booking> {

	@Autowired
	BookingRepository	repository;


	@Override
	public Booking convert(final String text) {
		Booking result;

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
