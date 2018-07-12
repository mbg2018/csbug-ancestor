package com.csbug.ancestor.config;

import com.csbug.ancestor.constant.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author ： Martin
 * Date : 18/1/16
 * Description : 枚举与数据库tinyint的映射类处理器
 * Version : 2.0
 */
public class EnumValueTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

	private Class<E> type;
	private final E[] enums;
	
	public EnumValueTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		if (!BaseEnum.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException(type.getName()
					+ " must implement the interface "
					+ BaseEnum.class.getName());
		}

		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null) {
			throw new IllegalArgumentException(type.getSimpleName()
					+ " does not represent an enum type.");
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
										JdbcType jdbcType) throws SQLException {
		ps.setInt(i, ((BaseEnum) parameter).getIndex());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return rs.wasNull() ? null : convert(rs.getInt(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.wasNull() ? null : convert(rs.getInt(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.wasNull() ? null : convert(cs.getInt(columnIndex));
	}

	private E convert(int value) {
		for (E e : enums) {
			if (value == ((BaseEnum) e).getIndex()) {
				return e;
			}
		}
		throw new IllegalArgumentException("Cannot convert " + value + " to "
				+ type.getSimpleName() + " by value.");
	}
}
