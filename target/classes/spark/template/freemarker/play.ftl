<#assign content>


<#include "board.ftl">

<form method="POST" action="/results">
  <textarea id="id_guesses" name="guesses" placeholder="Enter words here"></textarea>
  <input type="submit">
  <input type="hidden" name="board" value="${board.toString(',')}">
</form>

</#assign>
<#include "main.ftl">


