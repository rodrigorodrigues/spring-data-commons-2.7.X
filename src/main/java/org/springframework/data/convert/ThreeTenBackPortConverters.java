/*
 * Copyright 2015-2022 the original author or authors.
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

import static org.threeten.bp.DateTimeUtils.*;
import static org.threeten.bp.Instant.*;
import static org.threeten.bp.LocalDateTime.*;
import static org.threeten.bp.ZoneId.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

/**
 * Helper class to register {@link Converter} implementations for the ThreeTen Backport project in case it's present on
 * the classpath.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Jens Schauder
 * @author Mark Paluch
 * @see <a href="https://www.threeten.org/threetenbp">https://www.threeten.org/threetenbp</a>
 * @since 1.10
 * @deprecated since 2.3, use JSR-310 types as replacement for ThreeTenBackport.
 */
@Deprecated
public abstract class ThreeTenBackPortConverters {

	private static final boolean THREE_TEN_BACK_PORT_IS_PRESENT = ClassUtils.isPresent("org.threeten.bp.LocalDateTime",
			ThreeTenBackPortConverters.class.getClassLoader());
	private static final Collection<Class<?>> SUPPORTED_TYPES;

	static {

		SUPPORTED_TYPES = THREE_TEN_BACK_PORT_IS_PRESENT //
				? Arrays.asList(LocalDateTime.class, LocalDate.class, LocalTime.class, Instant.class, java.time.Instant.class)
				: Collections.emptySet();
	}

	/**
	 * Returns the converters to be registered. Will only return converters in case we're running on Java 8.
	 *
	 * @return
	 */
	public static Collection<Converter<?, ?>> getConvertersToRegister() {

		if (!THREE_TEN_BACK_PORT_IS_PRESENT) {
			return Collections.emptySet();
		}

		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(DateToLocalDateTimeConverter.INSTANCE);
		converters.add(LocalDateTimeToDateConverter.INSTANCE);
		converters.add(DateToLocalDateConverter.INSTANCE);
		converters.add(LocalDateToDateConverter.INSTANCE);
		converters.add(DateToLocalTimeConverter.INSTANCE);
		converters.add(LocalTimeToDateConverter.INSTANCE);
		converters.add(DateToInstantConverter.INSTANCE);
		converters.add(InstantToDateConverter.INSTANCE);
		converters.add(ZoneIdToStringConverter.INSTANCE);
		converters.add(StringToZoneIdConverter.INSTANCE);
		converters.add(LocalDateTimeToJsr310LocalDateTimeConverter.INSTANCE);
		converters.add(LocalDateTimeToJavaTimeInstantConverter.INSTANCE);
		converters.add(JavaTimeInstantToLocalDateTimeConverter.INSTANCE);

		return converters;
	}

	public static boolean supports(Class<?> type) {
		return SUPPORTED_TYPES.contains(type);
	}

	@Deprecated
	public static enum LocalDateTimeToJsr310LocalDateTimeConverter
			implements Converter<LocalDateTime, java.time.LocalDateTime> {

		INSTANCE;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@NonNull
		@Override
		public java.time.LocalDateTime convert(LocalDateTime source) {

			Date date = toDate(source.atZone(ZoneId.systemDefault()).toInstant());

			return Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE.convert(date);
		}
	}

	@Deprecated
	public static enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDateTime convert(Date source) {
			return ofInstant(toInstant(source), systemDefault());
		}
	}

	@Deprecated
	public static enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(LocalDateTime source) {
			return toDate(source.atZone(systemDefault()).toInstant());
		}
	}

	@Deprecated
	public static enum DateToLocalDateConverter implements Converter<Date, LocalDate> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDate convert(Date source) {
			return ofInstant(ofEpochMilli(source.getTime()), systemDefault()).toLocalDate();
		}
	}

	@Deprecated
	public static enum LocalDateToDateConverter implements Converter<LocalDate, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(LocalDate source) {
			return toDate(source.atStartOfDay(systemDefault()).toInstant());
		}
	}

	@Deprecated
	public static enum DateToLocalTimeConverter implements Converter<Date, LocalTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalTime convert(Date source) {
			return ofInstant(ofEpochMilli(source.getTime()), systemDefault()).toLocalTime();
		}
	}

	@Deprecated
	public static enum LocalTimeToDateConverter implements Converter<LocalTime, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(LocalTime source) {
			return toDate(source.atDate(LocalDate.now()).atZone(systemDefault()).toInstant());
		}
	}

	@Deprecated
	public static enum DateToInstantConverter implements Converter<Date, Instant> {

		INSTANCE;

		@NonNull
		@Override
		public Instant convert(Date source) {
			return toInstant(source);
		}
	}

	@Deprecated
	public static enum InstantToDateConverter implements Converter<Instant, Date> {

		INSTANCE;

		@NonNull
		@Override
		public Date convert(Instant source) {
			return toDate(source.atZone(systemDefault()).toInstant());
		}
	}

	@Deprecated
	public static enum LocalDateTimeToJavaTimeInstantConverter implements Converter<LocalDateTime, java.time.Instant> {

		INSTANCE;

		@NonNull
		@Override
		public java.time.Instant convert(LocalDateTime source) {
			return java.time.Instant.ofEpochMilli(source.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		}
	}

	@Deprecated
	public static enum JavaTimeInstantToLocalDateTimeConverter implements Converter<java.time.Instant, LocalDateTime> {

		INSTANCE;

		@NonNull
		@Override
		public LocalDateTime convert(java.time.Instant source) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.toEpochMilli()), ZoneOffset.systemDefault());
		}
	}

	@WritingConverter
	@Deprecated
	public static enum ZoneIdToStringConverter implements Converter<ZoneId, String> {

		INSTANCE;

		@NonNull
		@Override
		public String convert(ZoneId source) {
			return source.toString();
		}
	}

	@ReadingConverter
	@Deprecated
	public static enum StringToZoneIdConverter implements Converter<String, ZoneId> {

		INSTANCE;

		@NonNull
		@Override
		public ZoneId convert(String source) {
			return ZoneId.of(source);
		}
	}

}
