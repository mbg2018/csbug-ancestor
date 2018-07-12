package com.csbug.ancestor.config;

import com.csbug.ancestor.constant.BaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * 扩展默认的MyBatis工厂类，自动扫描指定的所有包前缀下实现IEnum接口的枚举类，并对其注册类型处理器EnumValueTypeHandler
 */
@Slf4j
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private String enumBasePackages; //指定需扫描的枚举类所在包的前缀
    private Resource[] mapperLocations;
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    private static ConcurrentHashMap<Class<? extends BaseEnum>, EnumValueTypeHandler<?>> TYPE_HANDLER_CACHE = new ConcurrentHashMap<>();

    public void setEnumBasePackages(String enumBasePackages) {
        this.enumBasePackages = enumBasePackages;
    }

    @Override
    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        super.setSqlSessionFactoryBuilder(sqlSessionFactoryBuilder);
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    /**
     *  解析并加载配置
     */
    private void loadMapperLocations(Configuration configuration) throws NestedIOException {
        if (!isEmpty(this.mapperLocations)) {
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation != null) {
                    try {
                        XMLMapperBuilder xmlMapperBuilder =
                                new XMLMapperBuilder(mapperLocation.getInputStream(),
                                        configuration,
                                        mapperLocation.toString(),
                                        configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    } catch (Exception e) {
                        throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
                    } finally {
                        ErrorContext.instance().reset();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        SqlSessionFactory oldSqlSessionFactory = super.buildSqlSessionFactory();
        Configuration configuration = oldSqlSessionFactory.getConfiguration();
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();  // 注意：type handler的注册必须在mapperLocations解析之前
        String[] enumPackages = parseEnumBasePackage();
        if (enumPackages != null) {
            Set<Class<? extends BaseEnum>> enumClasses = doScanEnumClass(enumPackages);
            if (enumClasses != null) {
                for (Class<? extends BaseEnum> cls : enumClasses) {
                    registry.register(cls, getEnumValueTypeHandlerInstance(cls));// 显示注册枚举处理器
                    if (log.isDebugEnabled()) {
                        log.debug("EnumValueTypeHandler is registered for type " + cls.getName());
                    }
                }
            }
        }
        loadMapperLocations(configuration);
        return this.sqlSessionFactoryBuilder.build(configuration);
    }

    /**
     * 获取枚举对应的handler实例，
     * 获取后该枚举对应的handle实例被缓存起来
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private EnumValueTypeHandler getEnumValueTypeHandlerInstance(Class<? extends BaseEnum> enumClass) {
        if (TYPE_HANDLER_CACHE.containsKey(enumClass)) {
            return TYPE_HANDLER_CACHE.get(enumClass);
        }
        EnumValueTypeHandler<?> handler = new EnumValueTypeHandler(enumClass);
        TYPE_HANDLER_CACHE.putIfAbsent(enumClass, handler);
        return handler;
    }

    /**
     * 搜索实现IEnum接口的枚举类
     */
    private Set<Class<? extends BaseEnum>> doScanEnumClass(String... enumBasePackages) {
        Set<Class<? extends BaseEnum>> filterClasses = new HashSet<>();
        ResolverUtil<BaseEnum> resolverUtil = new ResolverUtil<>();
        resolverUtil.findImplementations(BaseEnum.class, enumBasePackages);
        Set<Class<? extends BaseEnum>> handlerSet = resolverUtil.getClasses();
        filterClasses.addAll(handlerSet.stream().filter(Class::isEnum).collect(Collectors.toList()));
        return filterClasses;
    }

    private String[] parseEnumBasePackage() {
        return StringUtils.tokenizeToStringArray(this.enumBasePackages,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
    }


}
