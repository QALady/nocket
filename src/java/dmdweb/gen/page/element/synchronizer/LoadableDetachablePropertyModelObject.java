package dmdweb.gen.page.element.synchronizer;

import org.apache.wicket.model.LoadableDetachableModel;

import dmdweb.gen.page.DMDWebGenPageContext;

public class LoadableDetachablePropertyModelObject extends LoadableDetachableModel<Object> {

    private DMDWebGenPageContext ctx;

    public LoadableDetachablePropertyModelObject(DMDWebGenPageContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected Object load() {
        return ctx.getRefFactory().getRootReference().getRef().getRoot();
    }

}
