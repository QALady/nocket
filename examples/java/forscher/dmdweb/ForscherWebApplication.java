package forscher.dmdweb;

import dmdweb.gen.DMDGenericWebApplication;
import forscher.dmdweb.page.HomePage;
import gengui.util.I18n;
import gengui.util.I18nPropertyBasedImpl;

import org.apache.wicket.Page;

public class ForscherWebApplication extends DMDGenericWebApplication {

	@Override
	public void init() {
		super.init();
		// Let's try a gengui-based translation :-)
		I18n i18n = new I18nPropertyBasedImpl();
		System.out.println("UEBERSETZUNGSTEXT: " + i18n.translate("context.menu.copy"));
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see dmdweb.NocketWebApplication#getLoginPage()
	 */
	@Override
	public Class<? extends Page> getLoginPage() {
		return null;
	}

}
