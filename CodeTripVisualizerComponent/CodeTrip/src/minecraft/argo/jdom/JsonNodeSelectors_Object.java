package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Object extends LeafFunctor
{
    JsonNodeSelectors_Object()
    {
    }

    public boolean func_74640_a(JsonNode par1JsonNode)
    {
        return JsonNodeType.OBJECT == par1JsonNode.getType();
    }

    public String shortForm()
    {
        return "A short form object";
    }

    public Map func_74639_b(JsonNode par1JsonNode)
    {
        return par1JsonNode.getFields();
    }

    public String toString()
    {
        return "an object";
    }

    public Object typeSafeApplyTo(Object par1Obj)
    {
        return func_74639_b((JsonNode)par1Obj);
    }

    public boolean matchesNode(Object par1Obj)
    {
        return func_74640_a((JsonNode)par1Obj);
    }
}
