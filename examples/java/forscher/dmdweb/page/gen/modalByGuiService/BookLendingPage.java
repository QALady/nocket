package forscher.dmdweb.page.gen.modalByGuiService;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dmdweb.gen.page.GeneratedBinding;
import forscher.dmdweb.page.ForscherPage;

public class BookLendingPage extends ForscherPage {
    private static final long serialVersionUID = 1L;

    public BookLendingPage() {
        this(Model.of(new BookLending()));
    }

    public BookLendingPage(final IModel<BookLending> model) {
        super(model);
        final GeneratedBinding generatedBinding = new GeneratedBinding(this);
        generatedBinding.bind();
    }
}