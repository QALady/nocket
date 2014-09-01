package dmdweb.gen.page.element;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.jsoup.nodes.Element;

import dmdweb.gen.page.DMDWebGenPageContext;
import dmdweb.gen.page.element.synchronizer.BooleanChoiceRenderer;
import dmdweb.gen.page.element.synchronizer.BooleanRadioChoicesModel;
import dmdweb.gen.page.element.synchronizer.EnumChoicesRenderer;
import dmdweb.gen.page.element.synchronizer.GeneratedChoicesModel;
import dmdweb.gen.page.element.synchronizer.PrimitiveBooleanPropertyModel;
import dmdweb.gen.page.element.synchronizer.SynchronizerHelper;
import dmdweb.gen.page.visitor.PageElementVisitorI;

public class RadioInputElement extends AbstractDomainPageElement<Object> {

    public RadioInputElement(DMDWebGenPageContext context, Element element) {
        super(context, element);
    }

    @Override
    public IModel<Object> innerGetModel() {
        Class<?> returnType = getDomainElement().getMethod().getReturnType();
        if (returnType == boolean.class) {
            return new PrimitiveBooleanPropertyModel(getPropertyModelObject(), getPropertyExpression());
        } else {
            return new PropertyModel<Object>(getPropertyModelObject(), getPropertyExpression());
        }
    }

    public IModel<List<Object>> getChoicesModel() {
        Method method = getDomainElement().getMethod();
        Class<?> returnType = method.getReturnType();
        if (SynchronizerHelper.isBooleanType(method)) {
            return new BooleanRadioChoicesModel();
        } else {
            return new GeneratedChoicesModel(this);
        }
    }

    @Override
    public void accept(PageElementVisitorI visitor) {
        visitor.visitRadioInput(this);
    }

    public IChoiceRenderer getChoicesRenderer() {
        Method method = getDomainElement().getMethod();
        Class<?> returnType = method.getReturnType();
        if (returnType.isEnum()) {
            return new EnumChoicesRenderer(getContext());
        } else if (SynchronizerHelper.isBooleanType(method)) {
            return new BooleanChoiceRenderer(this);
        } else {
            return new ChoiceRenderer();
        }
    }

}
