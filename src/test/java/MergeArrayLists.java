import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.piranha.node.util.Utils;

import java.util.HashMap;

/**
 * Created by root on 4/14/16.
 */
public class MergeArrayLists {
    public static void main(String[] args) {
        String node1 = "{\"org.w3c.dom.Text\":\"org.w3c.dom.Text\",\"org.w3c.dom.html.HTMLFormElement\":\"org.w3c.dom.html.HTMLFormElement\",\"org.w3c.dom.views.DocumentView\":\"org.w3c.dom.views.DocumentView\",\"org.w3c.dom.css.RGBColor\":\"org.w3c.dom.css.RGBColor\",\"org.w3c.dom.xpath.XPathNSResolver\":\"org.w3c.dom.xpath.XPathNSResolver\",\"org.w3c.dom.css.CSSValue\":\"org.w3c.dom.css.CSSValue\",\"org.w3c.dom.html.HTMLImageElement\":\"org.w3c.dom.html.HTMLImageElement\",\"org.w3c.dom.html.HTMLLabelElement\":\"org.w3c.dom.html.HTMLLabelElement\",\"org.w3c.dom.css.CSSCharsetRule\":\"org.w3c.dom.css.CSSCharsetRule\",\"org.w3c.dom.DOMStringList\":\"org.w3c.dom.DOMStringList\",\"org.w3c.dom.css.CSSPrimitiveValue\":\"org.w3c.dom.css.CSSPrimitiveValue\",\"org.w3c.dom.ls.DOMImplementationLS\":\"org.w3c.dom.ls.DOMImplementationLS\",\"org.w3c.dom.events.EventTarget\":\"org.w3c.dom.events.EventTarget\",\"org.w3c.dom.traversal.TreeWalker\":\"org.w3c.dom.traversal.TreeWalker\",\"org.w3c.dom.css.CSSRuleList\":\"org.w3c.dom.css.CSSRuleList\",\"org.w3c.dom.Document\":\"org.w3c.dom.Document\",\"org.w3c.dom.css.CSSStyleDeclaration\":\"org.w3c.dom.css.CSSStyleDeclaration\",\"org.w3c.dom.stylesheets.MediaList\":\"org.w3c.dom.stylesheets.MediaList\",\"org.w3c.dom.ranges.DocumentRange\":\"org.w3c.dom.ranges.DocumentRange\",\"org.w3c.dom.ls.LSSerializer\":\"org.w3c.dom.ls.LSSerializer\",\"org.w3c.dom.css.CSSRule\":\"org.w3c.dom.css.CSSRule\",\"org.w3c.dom.css.DOMImplementationCSS\":\"org.w3c.dom.css.DOMImplementationCSS\",\"org.w3c.dom.css.CSSValueList\":\"org.w3c.dom.css.CSSValueList\",\"org.w3c.dom.html.HTMLPreElement\":\"org.w3c.dom.html.HTMLPreElement\",\"org.w3c.dom.css.CSSStyleSheet\":\"org.w3c.dom.css.CSSStyleSheet\",\"org.w3c.dom.html.HTMLLegendElement\":\"org.w3c.dom.html.HTMLLegendElement\",\"org.w3c.dom.DOMImplementation\":\"org.w3c.dom.DOMImplementation\",\"org.w3c.dom.events.MutationEvent\":\"org.w3c.dom.events.MutationEvent\",\"org.w3c.dom.html.HTMLTableCaptionElement\":\"org.w3c.dom.html.HTMLTableCaptionElement\",\"org.w3c.dom.html.HTMLOListElement\":\"org.w3c.dom.html.HTMLOListElement\",\"org.w3c.dom.views.AbstractView\":\"org.w3c.dom.views.AbstractView\",\"org.w3c.dom.events.EventException\":\"org.w3c.dom.events.EventException\",\"org.w3c.dom.css.CSSMediaRule\":\"org.w3c.dom.css.CSSMediaRule\",\"org.w3c.dom.html.HTMLFrameSetElement\":\"org.w3c.dom.html.HTMLFrameSetElement\",\"org.w3c.dom.html.HTMLMapElement\":\"org.w3c.dom.html.HTMLMapElement\",\"org.w3c.dom.html.HTMLScriptElement\":\"org.w3c.dom.html.HTMLScriptElement\",\"org.w3c.dom.xpath.XPathExpression\":\"org.w3c.dom.xpath.XPathExpression\",\"org.w3c.dom.xpath.XPathResult\":\"org.w3c.dom.xpath.XPathResult\",\"org.w3c.dom.html.HTMLDivElement\":\"org.w3c.dom.html.HTMLDivElement\",\"org.w3c.dom.html.HTMLCollection\":\"org.w3c.dom.html.HTMLCollection\",\"org.w3c.dom.stylesheets.StyleSheet\":\"org.w3c.dom.stylesheets.StyleSheet\",\"org.w3c.dom.html.HTMLBaseElement\":\"org.w3c.dom.html.HTMLBaseElement\",\"org.w3c.dom.traversal.NodeIterator\":\"org.w3c.dom.traversal.NodeIterator\",\"org.w3c.dom.traversal.DocumentTraversal\":\"org.w3c.dom.traversal.DocumentTraversal\",\"org.w3c.dom.html.HTMLIsIndexElement\":\"org.w3c.dom.html.HTMLIsIndexElement\",\"org.w3c.dom.ls.LSOutput\":\"org.w3c.dom.ls.LSOutput\",\"org.w3c.dom.DOMImplementationList\":\"org.w3c.dom.DOMImplementationList\",\"org.w3c.dom.events.Event\":\"org.w3c.dom.events.Event\",\"org.w3c.dom.html.HTMLTableColElement\":\"org.w3c.dom.html.HTMLTableColElement\",\"org.w3c.dom.traversal.NodeFilter\":\"org.w3c.dom.traversal.NodeFilter\",\"org.w3c.dom.html.HTMLAreaElement\":\"org.w3c.dom.html.HTMLAreaElement\",\"org.w3c.dom.html.HTMLSelectElement\":\"org.w3c.dom.html.HTMLSelectElement\",\"org.w3c.dom.html.HTMLModElement\":\"org.w3c.dom.html.HTMLModElement\",\"org.w3c.dom.html.HTMLMetaElement\":\"org.w3c.dom.html.HTMLMetaElement\",\"org.w3c.dom.html.HTMLDirectoryElement\":\"org.w3c.dom.html.HTMLDirectoryElement\",\"org.w3c.dom.html.HTMLHtmlElement\":\"org.w3c.dom.html.HTMLHtmlElement\",\"org.w3c.dom.stylesheets.StyleSheetList\":\"org.w3c.dom.stylesheets.StyleSheetList\",\"org.w3c.dom.ranges.Range\":\"org.w3c.dom.ranges.Range\",\"org.w3c.dom.css.CSSUnknownRule\":\"org.w3c.dom.css.CSSUnknownRule\",\"org.w3c.dom.html.HTMLMenuElement\":\"org.w3c.dom.html.HTMLMenuElement\",\"org.w3c.dom.html.HTMLParamElement\":\"org.w3c.dom.html.HTMLParamElement\",\"org.w3c.dom.html.HTMLBaseFontElement\":\"org.w3c.dom.html.HTMLBaseFontElement\",\"org.w3c.dom.DOMException\":\"org.w3c.dom.DOMException\",\"org.w3c.dom.Node\":\"org.w3c.dom.Node\",\"org.w3c.dom.TypeInfo\":\"org.w3c.dom.TypeInfo\",\"org.w3c.dom.html.HTMLDocument\":\"org.w3c.dom.html.HTMLDocument\",\"org.w3c.dom.html.HTMLElement\":\"org.w3c.dom.html.HTMLElement\",\"org.w3c.dom.NodeList\":\"org.w3c.dom.NodeList\",\"org.w3c.dom.html.HTMLButtonElement\":\"org.w3c.dom.html.HTMLButtonElement\",\"org.w3c.dom.NameList\":\"org.w3c.dom.NameList\",\"org.w3c.dom.html.HTMLTableRowElement\":\"org.w3c.dom.html.HTMLTableRowElement\",\"org.w3c.dom.events.DocumentEvent\":\"org.w3c.dom.events.DocumentEvent\",\"org.w3c.dom.css.Rect\":\"org.w3c.dom.css.Rect\",\"org.w3c.dom.html.HTMLDListElement\":\"org.w3c.dom.html.HTMLDListElement\",\"org.w3c.dom.xpath.XPathException\":\"org.w3c.dom.xpath.XPathException\",\"org.w3c.dom.xpath.XPathEvaluator\":\"org.w3c.dom.xpath.XPathEvaluator\",\"org.w3c.dom.css.ViewCSS\":\"org.w3c.dom.css.ViewCSS\",\"org.w3c.dom.html.HTMLTitleElement\":\"org.w3c.dom.html.HTMLTitleElement\",\"org.w3c.dom.ls.LSParser\":\"org.w3c.dom.ls.LSParser\",\"org.w3c.dom.Element\":\"org.w3c.dom.Element\",\"org.w3c.dom.css.Counter\":\"org.w3c.dom.css.Counter\",\"org.w3c.dom.html.HTMLUListElement\":\"org.w3c.dom.html.HTMLUListElement\",\"org.w3c.dom.events.EventListener\":\"org.w3c.dom.events.EventListener\",\"org.w3c.dom.ls.LSResourceResolver\":\"org.w3c.dom.ls.LSResourceResolver\",\"org.w3c.dom.ls.LSInput\":\"org.w3c.dom.ls.LSInput\",\"org.w3c.dom.css.CSSPageRule\":\"org.w3c.dom.css.CSSPageRule\",\"org.w3c.dom.css.CSS2Properties\":\"org.w3c.dom.css.CSS2Properties\",\"org.w3c.dom.css.CSSStyleRule\":\"org.w3c.dom.css.CSSStyleRule\"}";
        String node2 = "{\"org.w3c.dom.DOMImplementationSource\":\"org.w3c.dom.DOMImplementationSource\",\"org.w3c.dom.Text\":\"org.w3c.dom.Text\",\"org.w3c.dom.events.UIEvent\":\"org.w3c.dom.events.UIEvent\",\"org.w3c.dom.html.HTMLFormElement\":\"org.w3c.dom.html.HTMLFormElement\",\"org.w3c.dom.views.DocumentView\":\"org.w3c.dom.views.DocumentView\",\"org.w3c.dom.ranges.RangeException\":\"org.w3c.dom.ranges.RangeException\",\"org.w3c.dom.html.HTMLFieldSetElement\":\"org.w3c.dom.html.HTMLFieldSetElement\",\"org.w3c.dom.html.HTMLIFrameElement\":\"org.w3c.dom.html.HTMLIFrameElement\",\"org.w3c.dom.ls.LSException\":\"org.w3c.dom.ls.LSException\",\"org.w3c.dom.html.HTMLFontElement\":\"org.w3c.dom.html.HTMLFontElement\",\"org.w3c.dom.xpath.XPathNSResolver\":\"org.w3c.dom.xpath.XPathNSResolver\",\"org.w3c.dom.css.CSSImportRule\":\"org.w3c.dom.css.CSSImportRule\",\"org.w3c.dom.html.HTMLAppletElement\":\"org.w3c.dom.html.HTMLAppletElement\",\"org.w3c.dom.ls.LSLoadEvent\":\"org.w3c.dom.ls.LSLoadEvent\",\"org.w3c.dom.html.HTMLStyleElement\":\"org.w3c.dom.html.HTMLStyleElement\",\"org.w3c.dom.html.HTMLHeadElement\":\"org.w3c.dom.html.HTMLHeadElement\",\"org.w3c.dom.traversal.TreeWalker\":\"org.w3c.dom.traversal.TreeWalker\",\"org.w3c.dom.html.HTMLLIElement\":\"org.w3c.dom.html.HTMLLIElement\",\"org.w3c.dom.css.CSSRuleList\":\"org.w3c.dom.css.CSSRuleList\",\"org.w3c.dom.html.HTMLHRElement\":\"org.w3c.dom.html.HTMLHRElement\",\"org.w3c.dom.stylesheets.DocumentStyle\":\"org.w3c.dom.stylesheets.DocumentStyle\",\"org.w3c.dom.Document\":\"org.w3c.dom.Document\",\"org.w3c.dom.css.CSSStyleDeclaration\":\"org.w3c.dom.css.CSSStyleDeclaration\",\"org.w3c.dom.CDATASection\":\"org.w3c.dom.CDATASection\",\"org.w3c.dom.ls.LSSerializer\":\"org.w3c.dom.ls.LSSerializer\",\"org.w3c.dom.css.CSSStyleSheet\":\"org.w3c.dom.css.CSSStyleSheet\",\"org.w3c.dom.DOMImplementation\":\"org.w3c.dom.DOMImplementation\",\"org.w3c.dom.Entity\":\"org.w3c.dom.Entity\",\"org.w3c.dom.html.HTMLTableSectionElement\":\"org.w3c.dom.html.HTMLTableSectionElement\",\"org.w3c.dom.html.HTMLQuoteElement\":\"org.w3c.dom.html.HTMLQuoteElement\",\"org.w3c.dom.html.HTMLOptGroupElement\":\"org.w3c.dom.html.HTMLOptGroupElement\",\"org.w3c.dom.html.HTMLObjectElement\":\"org.w3c.dom.html.HTMLObjectElement\",\"org.w3c.dom.views.AbstractView\":\"org.w3c.dom.views.AbstractView\",\"org.w3c.dom.html.HTMLInputElement\":\"org.w3c.dom.html.HTMLInputElement\",\"org.w3c.dom.bootstrap.DOMImplementationRegistry\":\"org.w3c.dom.bootstrap.DOMImplementationRegistry\",\"org.w3c.dom.css.CSSFontFaceRule\":\"org.w3c.dom.css.CSSFontFaceRule\",\"org.w3c.dom.html.HTMLDOMImplementation\":\"org.w3c.dom.html.HTMLDOMImplementation\",\"org.w3c.dom.xpath.XPathExpression\":\"org.w3c.dom.xpath.XPathExpression\",\"org.w3c.dom.stylesheets.LinkStyle\":\"org.w3c.dom.stylesheets.LinkStyle\",\"org.w3c.dom.ls.LSParserFilter\":\"org.w3c.dom.ls.LSParserFilter\",\"org.w3c.dom.html.HTMLParagraphElement\":\"org.w3c.dom.html.HTMLParagraphElement\",\"org.w3c.dom.DOMLocator\":\"org.w3c.dom.DOMLocator\",\"org.w3c.dom.DocumentFragment\":\"org.w3c.dom.DocumentFragment\",\"org.w3c.dom.xpath.XPathNamespace\":\"org.w3c.dom.xpath.XPathNamespace\",\"org.w3c.dom.events.Event\":\"org.w3c.dom.events.Event\",\"org.w3c.dom.DOMError\":\"org.w3c.dom.DOMError\",\"org.w3c.dom.html.HTMLFrameElement\":\"org.w3c.dom.html.HTMLFrameElement\",\"org.w3c.dom.html.HTMLLinkElement\":\"org.w3c.dom.html.HTMLLinkElement\",\"org.w3c.dom.css.DocumentCSS\":\"org.w3c.dom.css.DocumentCSS\",\"org.w3c.dom.css.ElementCSSInlineStyle\":\"org.w3c.dom.css.ElementCSSInlineStyle\",\"org.w3c.dom.html.HTMLTableCellElement\":\"org.w3c.dom.html.HTMLTableCellElement\",\"org.w3c.dom.ranges.Range\":\"org.w3c.dom.ranges.Range\",\"org.w3c.dom.DOMConfiguration\":\"org.w3c.dom.DOMConfiguration\",\"org.w3c.dom.xpath.XPathException\":\"org.w3c.dom.xpath.XPathException\",\"org.w3c.dom.html.HTMLBodyElement\":\"org.w3c.dom.html.HTMLBodyElement\",\"org.w3c.dom.ls.LSParser\":\"org.w3c.dom.ls.LSParser\",\"org.w3c.dom.html.HTMLTextAreaElement\":\"org.w3c.dom.html.HTMLTextAreaElement\",\"org.w3c.dom.html.HTMLTableElement\":\"org.w3c.dom.html.HTMLTableElement\",\"org.w3c.dom.events.EventListener\":\"org.w3c.dom.events.EventListener\",\"org.w3c.dom.DOMErrorHandler\":\"org.w3c.dom.DOMErrorHandler\",\"org.w3c.dom.html.HTMLOptionElement\":\"org.w3c.dom.html.HTMLOptionElement\",\"org.w3c.dom.Notation\":\"org.w3c.dom.Notation\",\"org.w3c.dom.html.HTMLBRElement\":\"org.w3c.dom.html.HTMLBRElement\",\"org.w3c.dom.ls.LSSerializerFilter\":\"org.w3c.dom.ls.LSSerializerFilter\",\"org.w3c.dom.ls.LSProgressEvent\":\"org.w3c.dom.ls.LSProgressEvent\",\"org.w3c.dom.events.MouseEvent\":\"org.w3c.dom.events.MouseEvent\",\"org.w3c.dom.html.HTMLAnchorElement\":\"org.w3c.dom.html.HTMLAnchorElement\",\"org.w3c.dom.css.CSSValue\":\"org.w3c.dom.css.CSSValue\",\"org.w3c.dom.html.HTMLHeadingElement\":\"org.w3c.dom.html.HTMLHeadingElement\",\"org.w3c.dom.EntityReference\":\"org.w3c.dom.EntityReference\",\"org.w3c.dom.DOMStringList\":\"org.w3c.dom.DOMStringList\",\"org.w3c.dom.events.EventTarget\":\"org.w3c.dom.events.EventTarget\",\"org.w3c.dom.stylesheets.MediaList\":\"org.w3c.dom.stylesheets.MediaList\",\"org.w3c.dom.css.CSSRule\":\"org.w3c.dom.css.CSSRule\",\"org.w3c.dom.html.HTMLTableCaptionElement\":\"org.w3c.dom.html.HTMLTableCaptionElement\",\"org.w3c.dom.Comment\":\"org.w3c.dom.Comment\",\"org.w3c.dom.html.HTMLCollection\":\"org.w3c.dom.html.HTMLCollection\",\"org.w3c.dom.stylesheets.StyleSheet\":\"org.w3c.dom.stylesheets.StyleSheet\",\"org.w3c.dom.ls.LSOutput\":\"org.w3c.dom.ls.LSOutput\",\"org.w3c.dom.DOMImplementationList\":\"org.w3c.dom.DOMImplementationList\",\"org.w3c.dom.traversal.NodeFilter\":\"org.w3c.dom.traversal.NodeFilter\",\"org.w3c.dom.ProcessingInstruction\":\"org.w3c.dom.ProcessingInstruction\",\"org.w3c.dom.Attr\":\"org.w3c.dom.Attr\",\"org.w3c.dom.stylesheets.StyleSheetList\":\"org.w3c.dom.stylesheets.StyleSheetList\",\"org.w3c.dom.DOMException\":\"org.w3c.dom.DOMException\",\"org.w3c.dom.Node\":\"org.w3c.dom.Node\",\"org.w3c.dom.TypeInfo\":\"org.w3c.dom.TypeInfo\",\"org.w3c.dom.NamedNodeMap\":\"org.w3c.dom.NamedNodeMap\",\"org.w3c.dom.html.HTMLDocument\":\"org.w3c.dom.html.HTMLDocument\",\"org.w3c.dom.NodeList\":\"org.w3c.dom.NodeList\",\"org.w3c.dom.html.HTMLElement\":\"org.w3c.dom.html.HTMLElement\",\"org.w3c.dom.UserDataHandler\":\"org.w3c.dom.UserDataHandler\",\"org.w3c.dom.CharacterData\":\"org.w3c.dom.CharacterData\",\"org.w3c.dom.DocumentType\":\"org.w3c.dom.DocumentType\",\"org.w3c.dom.Element\":\"org.w3c.dom.Element\",\"org.w3c.dom.ls.LSInput\":\"org.w3c.dom.ls.LSInput\"}";


        Gson g = new Gson();
        JsonParser parser = new JsonParser();

        JsonObject node1Obj = parser.parse(node1).getAsJsonObject();
        JsonObject node2Obj = parser.parse(node2).getAsJsonObject();

        HashMap<String,String> node1Map = g.fromJson(node1Obj, Utils.hashMapType);
        HashMap<String,String> node2Map = g.fromJson(node2Obj, Utils.hashMapType);

        node1Map.putAll(node2Map);

        System.out.println(node1Map.values().size());
    }
}
