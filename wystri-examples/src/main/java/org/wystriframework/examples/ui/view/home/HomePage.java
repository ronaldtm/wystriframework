package org.wystriframework.examples.ui.view.home;

import java.util.concurrent.TimeUnit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wystriframework.annotation.impl.AnnotatedRecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.examples.command.SampleCommand;
import org.wystriframework.examples.ui.view.BasePage;
import org.wystriframework.form.crudgen.view.wicket.CrudgenPanel;
import org.wystriframework.form.formbuilder.EntityFormProcessorBehavior;
import org.wystriframework.ui.bootstrap.BSAlertFeedback;

public class HomePage extends BasePage {
    private static final long serialVersionUID = 1L;

    private SampleCommand     command          = new SampleCommand();

    public HomePage(final PageParameters parameters) {
        super(parameters);

        final BSAlertFeedback feedback = new BSAlertFeedback("feedback");

        final Form<?> form = new Form<Void>("form") {};

        form.setMultiPart(true);

        add(form

            .add(feedback)

            .add(new CrudgenPanel<>("crud", new AnnotatedRecordModel<>(() -> command)))

            .add(new AjaxButton("enviar", () -> "Enviar", form) {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    command.executar();
                    target.add(form);
                    target.appendJavaScript("console.log('submit');");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.add(form);
                    target.appendJavaScript("console.log('error');");
                }
            })

            .add(new EntityFormProcessorBehavior()));
    }

//    @Override
//    public void renderHead(IHeaderResponse response) {
//        super.renderHead(response);
//        response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
//        WystriConfiguration.get().getHeaderContributor().renderHead(response);
//    }
}
