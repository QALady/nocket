package org.nocket.gen.domain.visitor.properties;

import org.nocket.gen.domain.DMDWebGenContext;
import org.nocket.gen.domain.element.ButtonElement;
import org.nocket.gen.domain.element.CheckboxPropertyElement;
import org.nocket.gen.domain.element.ChoicerPropertyElement;
import org.nocket.gen.domain.element.HeadlineElement;
import org.nocket.gen.domain.element.HiddenPropertyElement;
import org.nocket.gen.domain.element.MultivaluePropertyElement;
import org.nocket.gen.domain.element.ResourceElement;
import org.nocket.gen.domain.element.SimplePropertyElement;
import org.nocket.gen.domain.visitor.AbstractDomainElementVisitor;
import org.nocket.gen.domain.visitor.DomainElementVisitorI;
import org.nocket.gen.domain.visitor.DummyVisitor;

import gengui.domain.AbstractDomainReference;

public class DelegatePropertiesVisitor<E extends AbstractDomainReference> extends AbstractDomainElementVisitor<E> {

    private final DomainElementVisitorI<E> delegate;

    public DelegatePropertiesVisitor(DMDWebGenContext<E> context) {
        super(context);
        if (context.getDomainProperties().isJFDLocalizationOnce()) {
            this.delegate = new DummyVisitor<E>(context);
        } else if (context.getDomainProperties().isLocalizationWicket()) {
            this.delegate = new WicketPropertiesVisitor<E>(context);
        } else {
            this.delegate = new GenguiPropertiesVisitor<E>(context);
        }
    }

    @Override
    public void visitSimpleProperty(SimplePropertyElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitChoicerProperty(ChoicerPropertyElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitCheckboxProperty(CheckboxPropertyElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitButton(ButtonElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitResource(ResourceElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitFieldsetOpen(HeadlineElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitMultivalueProperty(MultivaluePropertyElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void visitFieldsetClose() {
        delegate.visitFieldsetClose();
    }

    @Override
    public void visitHiddenProperty(HiddenPropertyElement<E> e) {
        e.accept(delegate);
    }

    @Override
    public void finish() {
        delegate.finish();
    }

}