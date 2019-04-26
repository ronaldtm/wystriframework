package org.wystriframework.core.wicket.component.jquery;

import static org.junit.Assert.*;
import static org.wystriframework.core.wicket.component.jquery.JQuery.*;

import org.junit.Test;

public class JQueryTest {

    @Test
    public void test() {
        assertEquals("setTimeout(function(){"
            + "$('div.panel')"
            + ".addClass('active')"
            + ".parent()"
            + ".find('input').attr('autocomplete', 'off').end()"
            + ".each(function(){ console.log(this); })"
            + ".hide()"
            + ".show('slideDown', 300)"
            + ".on('click', function(){ console.log('clicked!'); })"
            + ".one('doubleclick', function(){ $(this).remove(); })"
            + ".remove()"
            + ".removeClass('inactive')"
            + ".trigger('click')"
            + ";}, 1)",
            $("div.panel")
                .addClass("active")
                .appendRaw(".parent()")
                .find("input").attr("autocomplete", "off").end()
                .each("function(){ console.log(this); }")
                .hide()
                .show("slideDown", 300)
                .on("click", "function(){ console.log('clicked!'); }")
                .one("doubleclick", "function(){ $(this).remove(); }")
                .remove()
                .removeClass("inactive")
                .trigger("click")
                .asSetTimeoutCall(1));
    }

    @Test
    public void immutability() {
        JQuery $doc = $document();
        JQuery $docHidden = $document().addClass("hidden");
        JQuery $docVisible = $document().addClass("visible");
        JQuery $docPanels = $document().find("div.panel");
        JQuery $docPanelInputs = $docPanels.find("input");
        JQuery $docPanelLabels = $docPanels.find("label");

        assertEquals("$(document)", $doc.toString());
        assertEquals("$(document).addClass('hidden')", $docHidden.toString());
        assertEquals("$(document).addClass('visible')", $docVisible.toString());
        assertEquals("$(document).find('div.panel')", $docPanels.toString());
        assertEquals("$(document).find('div.panel').find('input')", $docPanelInputs.toString());
        assertEquals("$(document).find('div.panel').find('label')", $docPanelLabels.toString());

    }

}
