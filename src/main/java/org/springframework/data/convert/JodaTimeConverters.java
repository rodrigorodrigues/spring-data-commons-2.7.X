/*
 * Copyright 2013-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.convert;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

/**
 * Helper class to register JodaTime specific {@link Converter} implementations in case the library is present on the
 * classpath.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Jens Schauder
 * @author Mark Paluch
 * @deprecated since 2.3, use JSR-310 types as replacement for Joda-Time.
 */
@Deprecated
public abstract class JodaTimeConverters {

	private static final boolean JODA_TIME_IS_PRESENT = ClassUtils.isPresent("org.joda.time.LocalDate", null);

	/**
	 * Returns the converters to be registered. Will only return converters in case JodaTime is present on the class.
	 *
	 * @return
	 */
	public static Collection<Converter<?, ?>> getConvertersToRegister() {

		if (!JODA_TIME_IS_PRESENT) {
			return Collections.emptySet();
		}

		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(LocalDateToDateConverter.INSTANCE);
		converters.add(LocalDateTimeToDateConverter.INSTANCE);
		converters.add(DateTimeToDateConverter.INSTANCE);

		converters.add(DateToLocalDateConverter.INSTANCE);
		converters.add(DateToLocalDateTimeConverter.INSTANCE);
		converters.add(DateToDateTimeConverter.INSTANCE);

		converters.add(LocalDateTimeToJodaLocalDateTime.INSTANCE);
		converters.add(LocalDateTimeToJodaDateTime.INSTANCE);

		converters.add(InstantToJodaLocalDateTime.INSTANCE);
		converters.add(JodaLocalDateTimeToInstant.INSTANCE);

		converters.add(LocalDateTimeToJsr310Converter.INSTANCE);

		return converters;
	}

	@Deprecated
	public enum LocalDateTimeToJsr310Converter implements Converter<LocalDateTime, java.time.LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public java.time.LocalDateTime convert(LocalDateTime source) {
			return java.time.LocalDateTime.ofInstant(source.toDate().toInstant(), ZoneId.systemDefault());
		}
	}

	@Deprecated
	public enum LocalDateToDateConverter implements Converter<LocalDate, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(LocalDate source) {
			return source.toDate();
		}
	}

	@Deprecated
	public enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(LocalDateTime source) {
			return source.toDate();
		}
	}

	@Deprecated
	public enum DateTimeToDateConverter implements Converter<DateTime, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(DateTime source) {
			return source.toDate();
		}
	}

	@Deprecated
	public enum DateToLocalDateConverter implements Converter<Date, LocalDate> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDate convert(Date source) {
			return new LocalDate(source.getTime());
		}
	}

	@Deprecated
	public enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDateTime convert(Date source) {
			return new LocalDateTime(source.getTime());
		}
	}

	@Deprecated
	public enum DateToDateTimeConverter implements Converter<Date, DateTime> {

		INSTANCE;

		@NonNull
		@Override
		public DateTime convert(Date source) {
			return new DateTime(source.getTime());
		}
	}

	@ReadingConverter
	@Deprecated
	public enum LocalDateTimeToJodaLocalDateTime implements Converter<java.time.LocalDateTime, LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDateTime convert(java.time.LocalDateTime source) {
			return LocalDateTime.fromDateFields(Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(source));
		}
	}

	@Deprecated
	public enum InstantToJodaLocalDateTime implements Converter<java.time.Instant, LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDateTime convert(java.time.Instant source) {
			return LocalDateTime.fromDateFields(new Date(source.toEpochMilli()));
		}
	}

	@Deprecated
	public enum JodaLocalDateTimeToInstant implements Converter<LocalDateTime, Instant> {

		INSTANCE;

		@NonNull
		@Override
		public Instant convert(LocalDateTime source) {
			return Instant.ofEpochMilli(source.toDateTime().getMillis());
		}
	}

	@Deprecated
	public enum LocalDateTimeToJodaDateTime implements Converter<java.time.LocalDateTime, DateTime> {

		INSTANCE;

		@NonNull
		@Override
		public DateTime convert(java.time.LocalDateTime source) {
			return new DateTime(Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(source));
		}
	}
}
