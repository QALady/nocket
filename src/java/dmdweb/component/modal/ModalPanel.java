package dmdweb.component.modal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import dmdweb.component.button.DMDFormOverlayAjaxButton;
import dmdweb.gen.page.guiservice.CloserHandler;

@SuppressWarnings("serial")
public class ModalPanel extends Panel {

    private final DMDModalWindow dmdModalWindow;
    private final WebMarkupContainer emptyWebMarkup = new WebMarkupContainer("innerContent");
    private Panel content;
    private CloserHandler defaultCloserButtonCallback;

    public ModalPanel(String id, IModel<String> title, DMDModalWindow dmdModalWindow) {
        super(id, null);
        this.dmdModalWindow = dmdModalWindow;

        Form form = new Form("modalPanelform");
        add(form);
        form.add(new Label("title", title));
        DMDFormOverlayAjaxButton closeButton = new DMDFormOverlayAjaxButton("close", form) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (defaultCloserButtonCallback != null) {
                    defaultCloserButtonCallback.onSubmit(target);
                }
                super.onSubmit(target, form);
            }

            @Override
            public void onError(AjaxRequestTarget target, Form<?> form) {
                if (defaultCloserButtonCallback != null) {
                    defaultCloserButtonCallback.onSubmit(target);
                }
                super.onError(target, form);
            }

            @Override
            public void onConfigure() {
                boolean showCloseButton = true;
                if (defaultCloserButtonCallback != null) {
                    showCloseButton = !defaultCloserButtonCallback.hideCloseButton();
                }
                setVisibilityAllowed(showCloseButton);
                super.onConfigure();
            }
        };
        form.add(closeButton);
        WebMarkupContainer divModal = new WebMarkupContainer("innerInnerModal") {

            @Override
            /**
             * Austauschen des Panels
             */
            protected void onBeforeRender() {
                if (content != null) {
                    this.addOrReplace(content);
                } else {
                    this.addOrReplace(emptyWebMarkup);
                }
                super.onBeforeRender();
            }
        };
        form.add(divModal);
        setOutputMarkupId(true);
    }

    public void setContent(Panel content) {
        this.content = content;
    }

    public void close(AjaxRequestTarget target) {
        dmdModalWindow.close(target);
    }

    public void setDefaultCloserButtonCallback(CloserHandler closerHandler) {
        this.defaultCloserButtonCallback = closerHandler;
    }
}
