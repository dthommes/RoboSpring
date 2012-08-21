/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.junit.Test;
import org.springframework.util.FileCopyUtils;

/**
 * @author Juergen Hoeller
 * @author Daniel Thommes
 * @since 09.09.2004
 */
public class ResourceTests {

	@Test
	public void testByteArrayResource() throws IOException {
		Resource resource = new ByteArrayResource("testString".getBytes());
		assertTrue(resource.exists());
		assertFalse(resource.isOpen());
		String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
		assertEquals("testString", content);
		assertEquals(resource, new ByteArrayResource("testString".getBytes()));
	}

	@Test
	public void testByteArrayResourceWithDescription() throws IOException {
		Resource resource = new ByteArrayResource("testString".getBytes(), "my description");
		assertTrue(resource.exists());
		assertFalse(resource.isOpen());
		String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
		assertEquals("testString", content);
		assertEquals("my description", resource.getDescription());
		assertEquals(resource, new ByteArrayResource("testString".getBytes()));
	}

	@Test
	public void testInputStreamResource() throws IOException {
		InputStream is = new ByteArrayInputStream("testString".getBytes());
		Resource resource = new InputStreamResource(is);
		assertTrue(resource.exists());
		assertTrue(resource.isOpen());
		String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
		assertEquals("testString", content);
		assertEquals(resource, new InputStreamResource(is));
	}

	@Test
	public void testInputStreamResourceWithDescription() throws IOException {
		InputStream is = new ByteArrayInputStream("testString".getBytes());
		Resource resource = new InputStreamResource(is, "my description");
		assertTrue(resource.exists());
		assertTrue(resource.isOpen());
		String content = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
		assertEquals("testString", content);
		assertEquals("my description", resource.getDescription());
		assertEquals(resource, new InputStreamResource(is));
	}

	@Test
	public void testClassPathResource() throws IOException {
		Resource resource = new ClassPathResource("org/springframework/core/io/example.properties");
		doTestResource(resource);
		Resource resource2 = new ClassPathResource("org/springframework/core/../core/io/./example.properties");
		assertEquals(resource, resource2);
		Resource resource3 = new ClassPathResource("org/springframework/core/").createRelative("../core/io/./example.properties");
		assertEquals(resource, resource3);

		// Check whether equal/hashCode works in a HashSet.
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(resource);
		resources.add(resource2);
		assertEquals(1, resources.size());
	}

	@Test
	public void testClassPathResourceWithClassLoader() throws IOException {
		Resource resource =
				new ClassPathResource("org/springframework/core/io/example.properties", getClass().getClassLoader());
		doTestResource(resource);
		assertEquals(resource,
				new ClassPathResource("org/springframework/core/../core/io/./example.properties", getClass().getClassLoader()));
	}

	@Test
	public void testClassPathResourceWithClass() throws IOException {
		Resource resource = new ClassPathResource("example.properties", getClass());
		doTestResource(resource);
		assertEquals(resource, new ClassPathResource("example.properties", getClass()));
	}

	@Test
	public void testUrlResource() throws IOException {
		Resource resource = new UrlResource(getClass().getResource("example.properties"));
		doTestResource(resource);
		assertEquals(new UrlResource(getClass().getResource("example.properties")), resource);
		Resource resource2 = new UrlResource("file:core/io/example.properties");
		assertEquals(resource2, new UrlResource("file:core/../core/io/./example.properties"));
	}

	private void doTestResource(Resource resource) throws IOException {
		assertEquals("example.properties", resource.getFilename());
		assertTrue(resource.getURL().getFile().endsWith("example.properties"));

		/*
		Resource relative1 = resource.createRelative("ClassPathResource.class");
		assertEquals("ClassPathResource.class", relative1.getFilename());
		assertTrue(relative1.getURL().getFile().endsWith("ClassPathResource.class"));
		assertTrue(relative1.exists());

		Resource relative2 = resource.createRelative("support/ResourcePatternResolver.class");
		assertEquals("ResourcePatternResolver.class", relative2.getFilename());
		assertTrue(relative2.getURL().getFile().endsWith("ResourcePatternResolver.class"));
		assertTrue(relative2.exists());*/

		/*
		Resource relative3 = resource.createRelative("../SpringVersion.class");
		assertEquals("SpringVersion.class", relative3.getFilename());
		assertTrue(relative3.getURL().getFile().endsWith("SpringVersion.class"));
		assertTrue(relative3.exists());
		*/
	}

	@Test
	public void testClassPathResourceWithRelativePath() throws IOException {
		Resource resource = new ClassPathResource("dir/");
		Resource relative = resource.createRelative("subdir");
		assertEquals(new ClassPathResource("dir/subdir"), relative);
	}

	@Test
	public void testUrlResourceWithRelativePath() throws IOException {
		Resource resource = new UrlResource("file:dir/");
		Resource relative = resource.createRelative("subdir");
		assertEquals(new UrlResource("file:dir/subdir"), relative);
	}

	/*
	 * @Test
	public void testNonFileResourceExists() throws Exception {
		Resource resource = new UrlResource("http://www.springframework.org");
		assertTrue(resource.exists());
	}
	*/

	@Test
	public void testAbstractResourceExceptions() throws Exception {
		final String name = "test-resource";

		Resource resource = new AbstractResource() {
			public String getDescription() {
				return name;
			}
			public InputStream getInputStream() {
				return null;
			}
		};

		try {
			resource.getURL();
			fail("FileNotFoundException should have been thrown");
		}
		catch (FileNotFoundException ex) {
			assertTrue(ex.getMessage().indexOf(name) != -1);
		}
		try {
			resource.getFile();
			fail("FileNotFoundException should have been thrown");
		}
		catch (FileNotFoundException ex) {
			assertTrue(ex.getMessage().indexOf(name) != -1);
		}
		try {
			resource.createRelative("/testing");
			fail("FileNotFoundException should have been thrown");
		}
		catch (FileNotFoundException ex) {
			assertTrue(ex.getMessage().indexOf(name) != -1);
		}
		try {
			resource.getFilename();
			fail("IllegalStateException should have been thrown");
		}
		catch (IllegalStateException ex) {
			assertTrue(ex.getMessage().indexOf(name) != -1);
		}
	}

}
