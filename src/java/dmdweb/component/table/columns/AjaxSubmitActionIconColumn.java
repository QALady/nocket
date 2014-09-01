package dmdweb.component.table.columns;

import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;

import dmdweb.component.button.DMDOnClickIndicatorAttributeModifier;
import dmdweb.component.table.GenericDataTablePanel;

/**
 * Column with ajax link displayed as image.
 * 
 * @param <T>
 *            Model object type representing one line in
 *            {@link GenericDataTablePanel}.
 */
public abstract class AjaxSubmitActionIconColumn<T> extends DMDAbstractColumn<T> {
    private static final long serialVersionUID = 1L;
    public static final String ICON_ENABLED = "icon-enabled.png";
    public static final String ICON_DISBALED = "icon-disabled.png";
    public static final String ACTION_COLUMN = "dmdweb-action-column";

    private final Form<?> form;

    protected final String enabledIconPropertyKey;
    protected final String disabledIconPropertyKey;

    public AjaxSubmitActionIconColumn(IModel<String> headerLabel, Form<?> form) {
        super(headerLabel, null);
        this.form = form;
        this.enabledIconPropertyKey = null;
        this.disabledIconPropertyKey = null;
    }

    public AjaxSubmitActionIconColumn(IModel<String> headerLabel, Form<?> form, String enabledContextIcon,
            String disabledContextIcon) {
        super(headerLabel, null);
        this.form = form;
        this.enabledIconPropertyKey = enabledContextIcon;
        this.disabledIconPropertyKey = disabledContextIcon;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new LinkImagePanel<T>(componentId, this, rowModel, form));
    }

    @Override
    public String getCssClass() {
        return ACTION_COLUMN;
    }

    /**
     * Defines whether link is enabled. You may decide to implement special
     * behavior for each line, depending on the model object.
     * 
     * @param model
     *            Model of the line.
     * 
     * @return Whether link is enabled. Default is true.
     */
    protected boolean isEnabled(IModel<T> model) {
        return true;
    }

    /**
     * Defines whether the link has a @Forced annotation and the validation
     * should be skipped. You may decide to implement special behavior for each
     * line, depending on the model object.
     * 
     * @param model
     *            Model of the line
     * @return Whether link has a @Forced annotation. Default is false.
     */
    protected boolean isForced(IModel<T> model) {
        return false;
    }

    /**
     * Defines whether link is visible. You may decide to implement special
     * behavior for each line, depending on the model object.
     * 
     * @param model
     *            Model of the line.
     * 
     * @return Whether link is visible. Default is true.
     */
    protected boolean isVisible(IModel<T> model) {
        return true;
    }

    /**
     * Returns image for the link as Wicket resource. The name of the image is
     * determined from property file. If property is not found or name is empty,
     * default icons will be displayed. Method differentiates between enabled
     * and disabled icons.
     * 
     * @param model
     *            Model of the line.
     * 
     * @return Image as Wicket resource.
     */
    protected IResource getIcon(IModel<T> model, String resourceName) {
        if (!StringUtils.isEmpty(resourceName)) {
            return new ContextRelativeResource(resourceName);
        }
        return getDefaultIcon(model);
    }

    /**
     * Returns name of the icon from property key. Default implementation
     * resolves it with Wickets localizer. Overwrite it if you want something
     * else.
     * 
     * @param propertyKey
     * 
     * @return name Path to the icon from root context. For instance:
     *         "img/image.gif" will be lookup in "webapp/img/image.gif"
     */
    protected String getIconResourceName(IModel<T> model, Component c) {
        try {
            String propertyKey = this.isEnabled(model) ? enabledIconPropertyKey : disabledIconPropertyKey;
            return Localizer.get().getString(propertyKey, c.getPage());
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * Returns reference to default icons. Method differentiates between enabled
     * and disabled icons.
     * 
     * @param model
     *            Model of the line.
     * 
     * @return Image as Wicket resource.
     */

    protected IResource getDefaultIcon(IModel<T> model) {
        if (isEnabled(model)) {
            return new PackageResourceReference(AjaxSubmitActionIconColumn.class, ICON_ENABLED).getResource();
        }
        return new PackageResourceReference(AjaxSubmitActionIconColumn.class, ICON_DISBALED).getResource();
    }

    /**
     * Returns tool-tip for the icon.
     * 
     * @param model
     *            Model of the line.
     * 
     * @return Default is null.
     */
    protected String getTooltip(IModel<T> model) {
        return null;
    }

    /**
     * Called when a link is clicked.
     * 
     * @param model
     *            Model of the line.
     */
    protected abstract void onSubmit(IModel<T> model, AjaxRequestTarget target);

    protected abstract void onError(IModel<T> model, AjaxRequestTarget target);

    private static class LinkImagePanel<T> extends Panel {
        private static final long serialVersionUID = 1L;
        private static final String LINK = "link";
        private static final String ICON = "icon";
        private static final String TITLE = "title";

        public LinkImagePanel(String id, final AjaxSubmitActionIconColumn<T> column, final IModel<T> rowModel,
                Form<?> form) {
            super(id);

            add(new AjaxSubmitLink(LINK, form) {
                private static final long serialVersionUID = 1L;

                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript(DMDOnClickIndicatorAttributeModifier.getBlockerRemoveScript());
                    column.onSubmit(rowModel, target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript(DMDOnClickIndicatorAttributeModifier.getBlockerRemoveScript());
                    column.onError(rowModel, target);
                }

                @Override
                public boolean isEnabled() {
                    return column.isEnabled(rowModel);
                }

                @Override
                public boolean isVisible() {
                    return column.isVisible(rowModel);
                }

                /**
                 * set DefaultFormProcessing to <code>false</code> when a @Forced
                 * annotation is set in order to skip the validation of this row
                 */
                @Override
                public boolean getDefaultFormProcessing() {
                    return !column.isForced(rowModel);
                }

                /**
                 * @see org.apache.wicket.Component#onInitialize()
                 */
                @Override
                protected void onInitialize() {
                    super.onInitialize();
                    final WebComponent icon = createIcon(column, rowModel);
                    final String tooltip = column.getTooltip(rowModel);
                    if (tooltip != null) {
                        icon.add(new AttributeAppender(TITLE, tooltip));
                    }
                    add(icon);
                }

                private WebComponent createIcon(final AjaxSubmitActionIconColumn<T> column, final IModel<T> rowModel) {
                    String resourceName = column.getIconResourceName(rowModel, this);
                    if (StringUtils.isEmpty(resourceName)) {
                        return new Image(ICON, column.getIcon(rowModel, resourceName)) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected boolean shouldAddAntiCacheParameter() {
                                return WebApplication.get().usesDevelopmentConfig();
                            }
                        };
                    } else {
                        return new ContextImage(ICON, resourceName);
                    }
                }
            }.add(new DMDOnClickIndicatorAttributeModifier(this)));
        }
    }
}
