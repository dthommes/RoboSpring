# RoboSpring
RoboSpring is a (real) port of the [Spring Framework](http://www.springsource.org/spring-framework) to the Android platform. Additionally it offers preliminary support for functionality introduced by [RoboGuice](https://github.com/roboguice/roboguice) like injecting View references into Activities and more.

RoboSpring is based on version 3.1.0 RELEASE of Spring's **core**, **beans**, **context** and **aop** components. It offers the following functionality:

* Configure application components with a Spring configuration file (XML)
* Autowire your Android components with beans from the Spring application context.
* Inject the Android application context into your Spring Beans.
* Inject views into Activities.
* … and more

### License
RoboSpring is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).

## Getting Started

 * Download the latest RoboSpring release ( **robospring-X.X.X.jar** from the release page here on GitHub). Put this file into your application's classpath (e.g. by just dropping it into an Android Application Project's `lib` folder.)
 * Create a Spring configuration - for simplicity use the RoboSpring default location by adding a file named `applicationContext.xml` into the root of your classpath (src-folder). Add the following content to get started:
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/util
           http://www.springframework.org/schema/util/spring-util.xsd">
           
	<bean id="exampleBean" class="java.net.URL">
		<constructor-arg value="http://www.robospring.org" />
	</bean>
           
	<!-- define your beans here, use 'androidContext' to get a reference to your app's application context -->        
           
</beans>
```
* Let RoboSpring inject references to your beans into your Activities (or other application components). Just call `RoboSpring.autowire(this)` and let RoboSpring inject properties you have annotated with the `Autowired` annotation as shown below:
  
```java
	@Autowired
	private URL serviceUrl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		RoboSpring.autowire(this);
		
		System.out.println(serviceUrl);
	}

```
* After you have called RoboSpring's autowire method, you can use the injected beans. Well done!

For more info you can also refer to Spring's documentation. As RoboSpring is build on 3.1.0 RELEASE, you should refer to:

http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/


## Get a reference to the Android Context
You can get a reference to the Android application context to inject it into your Spring Beans easily. The Android application context is pre-defined in RoboSpring's application context under the name `androidContext`. So e.g. you can do the following:
  
```java

	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}
```

```xml
	<bean id="myBean" class="org.your.class.RequiringTheContext">
		<property name="context" ref="androidContext" />
	</bean>
```

## Inject more than Spring Beans
RoboSpring additionally has preliminary support for **RoboGuice** annotations. You can e.g. inject views from you layout into your Activity. For this we even use the same annotations, so migrating from RoboGuice to RoboSpring should be easy.

When you have the following view in your layout (it's `id` is `textView`)...

```xml
    <TextView
        android:id="@+id/textView"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />
```
… all you have to do, is define a property of the same name and annotate it with the `InjectView` annotation and let `RoboSpring.autowire(…)` do the rest:
        
```java
	@InjectView
	private TextView textView;
```
If you are familiar with RoboGuice, you will notice, that `InjectView` has no mandatory id attribute in RoboSpring. If you leave it away as shown, RoboSpring will deviate it from the property's name. Easy, isn't it?

**PLEASE NOTE**: You must call `setContentView(...)` in your Activity before calling `RoboSpring.autowire(this)`. Otherwise, RoboSpring does not know, where to look for the views!


**HAPPY INJECTING!**

Daniel