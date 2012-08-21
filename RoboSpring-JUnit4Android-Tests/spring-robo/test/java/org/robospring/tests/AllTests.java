/**
 * Created on 10.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.beans.ExtendedBeanInfoTests;
import org.springframework.beans.factory.xml.support.CustomNamespaceHandlerTests;
import org.springframework.context.config.ContextNamespaceHandlerTests;
import org.springframework.context.conversionservice.ConversionServiceContextConfigTests;
import org.springframework.context.event.ApplicationContextEventTests;
import org.springframework.context.event.LifecycleEventTests;
import org.springframework.context.support.ApplicationContextLifecycleTests;
import org.springframework.context.support.BeanFactoryPostProcessorTests;
import org.springframework.context.support.ClassPathXmlApplicationContextTests;
import org.springframework.context.support.ConversionServiceFactoryBeanTests;
import org.springframework.context.support.DefaultLifecycleProcessorTests;
import org.springframework.context.support.GenericApplicationContextTests;
import org.springframework.context.support.GenericXmlApplicationContextTests;
import org.springframework.context.support.PropertyResourceConfigurerIntegrationTests;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurerTests;
import org.springframework.context.support.ResourceBundleMessageSourceTests;
import org.springframework.context.support.SerializableBeanFactoryMemoryLeakTests;
import org.springframework.context.support.SimpleThreadScopeTest;
import org.springframework.context.support.Spr7283Tests;
import org.springframework.context.support.Spr7816Tests;
import org.springframework.context.support.StaticApplicationContextMulticasterTests;
import org.springframework.context.support.StaticApplicationContextTests;
import org.springframework.context.support.StaticMessageSourceTests;

/**
 *
 *
 * @author Daniel Thommes
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

		/*************************************************************
		 * spring-beans
		 *************************************************************/
		//org.springframework.beans
		/*BeanUtilsTests.class,
		BeanWrapperAutoGrowingTests.class,
		BeanWrapperEnumTests.class,
		BeanWrapperGenericsTests.class,
		BeanWrapperTests.class,
		ConcurrentBeanWrapperTests.class,
		DirectFieldAccessorTests.class,*/
		ExtendedBeanInfoTests.class,
		/*MutablePropertyValuesTests.class,
		PropertyAccessorUtilsTests.class,*/

		//org.springframework.beans.annotation
		/*AnnotationBeanWiringInfoResolverTests.class,
		RequiredAnnotationBeanPostProcessorTests.class,*/

		//org.springframework.beans.factory
		/*BeanFactoryUtilsTests.class,
		ConcurrentBeanFactoryTests.class,
		DefaultListableBeanFactoryTests.class,
		FactoryBeanLookupTests.class,
		FactoryBeanTests.class,
		SharedBeanRegistryTests.class,
		Spr5475Tests.class,

	    //org.springframework.beans.support
		PagedListHolderTests.class,
		PropertyComparatorTests.class,*/

		/*************************************************************
		 * spring-core
		 *************************************************************/
		// org.springframework.core
		/*AttributeAccessorSupportTests.class,
		BridgeMethodResolverTests.class,
		CollectionFactoryTests.class,
		ConstantsTests.class,
		ConventionsTests.class,
		ExceptionDepthComparatorTests.class,
		GenericCollectionTypeResolverTests.class,
		GenericTypeResolverTests.class,
		MethodParameterTests.class,
		NestedExceptionTests.class,
		OrderComparatorTests.class,
		PrioritizedParameterNameDiscovererTests.class,

		// org.springframework.core.convert
		TypeDescriptorTests.class,

		// org.springframework.core.convert.support
		CollectionToCollectionConverterTests.class,
		DefaultConversionTests.class,
		GenericConversionServiceTests.class,
		MapToMapConverterTests.class,

		// org.springframework.core.env
		CustomEnvironmentTests.class,
		PropertySourcesPropertyResolverTests.class,
		PropertySourcesTests.class,
		PropertySourceTests.class,
		StandardEnvironmentTests.class,
		SystemEnvironmentPropertySourceTests.class,

		// org.springframework.core.io
		ClassPathResourceTests.class,
		ResourceEditorTests.class,
		ResourceTests.class,

		// org.springframework.core.io.support
		PathMatchingResourcePatternResolverTests.class,
		ResourceArrayPropertyEditorTests.class,
		ResourcePropertySourceTests.class,

		//org.springframework.core.serializer
		SerializationConverterTests.class,

		// org.springframework.core.style
		ToStringCreatorTests.class,

		//org.springframework.core.task
		SimpleAsyncTaskExecutorTests.class,

		//org.springframework.util
		AntPathMatcherTests.class,
		AssertTests.class,
		AutoPopulatingListTests.class,
		CachingMapDecoratorTests.class,
		ClassUtilsTests.class,
		CollectionUtilsTests.class,
		CompositeIteratorTests.class,
		DigestUtilsTests.class,
		FileCopyUtilsTests.class,
		LinkedMultiValueMapTests.class,
		MethodInvokerTests.class,
		NumberUtilsTests.class,
		ObjectUtilsTests.class,
		PatternMatchUtilsTests.class,
		PropertiesPersisterTests.class,
		PropertyPlaceholderHelperTests.class,
		ReflectionUtilsTests.class,
		ResourceUtilsTests.class,
		SerializationTestUtils.class,
		SerializationUtilsTests.class,
		StopWatchTests.class,
		StringUtilsTests.class,
		SystemPropertyUtilsTests.class,
		TypeUtilsTests.class,

		//org.springframework.util.comparator
		ComparatorTests.class,*/
		


		/*************************************************************
		 * spring-context
		 *************************************************************/
		// org.springframework.beans.factory.access
		// SingletonBeanFactoryLocatorTests.class,

		// org.springframework.beans.factory.xml
		// F
		// QualifierAnnotationTests.class,
		// F
		//SimplePropertyNamespaceHandlerWithExpressionLanguageTests.class,
		// F
		// XmlBeanFactoryTests.class,

		//org.springframework.beans.factory.xml.support
		CustomNamespaceHandlerTests.class,

		//org.springframework.context.config
		ContextNamespaceHandlerTests.class,

		//org.springframework.context.conversionservice
		ConversionServiceContextConfigTests.class,

		//org.springframework.context.event
		ApplicationContextEventTests.class,
		LifecycleEventTests.class,

		//org.springframework.context.support
		ApplicationContextLifecycleTests.class,
		BeanFactoryPostProcessorTests.class,
		ClassPathXmlApplicationContextTests.class,
		ConversionServiceFactoryBeanTests.class,
		DefaultLifecycleProcessorTests.class,
		GenericApplicationContextTests.class,
		GenericXmlApplicationContextTests.class,
		PropertyResourceConfigurerIntegrationTests.class,
		PropertySourcesPlaceholderConfigurerTests.class,
		ResourceBundleMessageSourceTests.class,
		SerializableBeanFactoryMemoryLeakTests.class,
		SimpleThreadScopeTest.class,
		Spr7283Tests.class,
		Spr7816Tests.class,
		StaticApplicationContextMulticasterTests.class,
		StaticApplicationContextTests.class,
		StaticMessageSourceTests.class,

})
public class AllTests {

}
