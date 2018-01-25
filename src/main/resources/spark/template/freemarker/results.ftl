<#assign content>

<#include "board.ftl">
<p>
  Your score was ${score}.
</p>

<#if 0 < good?size >
You scored with these:
<ul>
<#list good as word>
   <li>${word}
</#list>
</ul>
</#if>

<#if 0 < bad?size >
These words didn't score
<ul>
<#list bad as word>
   <li>${word}
</#list>
</ul>
</#if>

<#if 0 < missed?size >
You missed these
<ul>
<#list missed as word>
   <li>${word}
</#list>
</ul>
</#if>

<a href="/play">Play another board.</a>

</#assign>
<#include "main.ftl">

