/**
 * Created on 26.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.android.tests.context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.robospring.RoboSpring;
import org.robospring.android.tests.InjectedBean;
import org.springframework.context.support.AbstractXmlApplicationContext;

import android.content.res.AssetManager;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 *
 *
 * @author Daniel Thommes
 */
public class ContextConfigLocationTest extends AndroidTestCase {

	public void testLoadConfiguredConfig() throws Exception {
		AbstractXmlApplicationContext context = RoboSpring.getContext();
		assertNotNull(context);
		InjectedBean bean = context.getBean(InjectedBean.class);
		assertNotNull(bean);
		assertEquals("Configured", bean.getName());
	}

	public void testLoadConfigFromClasspath() throws Exception {
		AbstractXmlApplicationContext context = RoboSpring
				.getContext("org/robospring/android/tests/context/classpath-context.xml");
		assertNotNull(context);
		InjectedBean bean = context.getBean(InjectedBean.class);
		assertNotNull(bean);
		assertEquals("Classpath", bean.getName());
	}

	public void testLoadConfigFromClasspathWithScheme() throws Exception {
		AbstractXmlApplicationContext context = RoboSpring
				.getContext("classpath:/org/robospring/android/tests/context/classpath-context.xml");
		assertNotNull(context);
		InjectedBean bean = context.getBean(InjectedBean.class);
		assertNotNull(bean);
		assertEquals("Classpath", bean.getName());
	}

	public void testLoadConfigFromFile() throws Exception {
		String dirName = Environment.getExternalStorageDirectory()
				+ "/robospring";
		File dir = new File(dirName);
		dir.mkdirs();

		File externalFile = new File(dir, "file-context.xml");
		copyFile("file-context.xml", externalFile.getPath());

		// TODO expand externalstoragedir
		AbstractXmlApplicationContext context = RoboSpring.getContext("file:"
				+ externalFile.getPath());

		assertNotNull(context);
		InjectedBean bean = context.getBean(InjectedBean.class);
		assertNotNull(bean);
		assertEquals("File", bean.getName());
	}

	private void copyFile(String sourceAsset, String targetName) {
		AssetManager assetManager = getContext().getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(sourceAsset);

			out = new FileOutputStream(targetName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
