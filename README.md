As most people eventually do, I'm implementing my own web framework.

Ostensibly, a web framework should help you make websites.  However, I
find that, for me, they don't so much help as much as frustrate as
they add little more than syntactic variation on MVC with various
quirks.

I find myself asking basic questions about parts of a website:

a) What links here?
  1) What states are required to be here?
b) To what does this link?
  1) What state is required to visit those links?
c) Are data being used consistently across the
  model-view-controller/presentation?
d) How do I know what data's available at runtime at this point in this
  page?

I do not want to see non-essential markup around the intended display
of data.  I do not want to go hunting for those other pieces of the
website that may be related to the thing I'm working on.  I do not
want to guess, or lookup in documentation, at what "conventions" are
in effect.

I want to have different visualizations about the structure and
control flow of my website/application.  I do not want to repeat
myself.  I do not want to repeat myself.  I repeat, I do not want to
repeat myself.

I do not want to have to struggle to find out what the framework
actually does to the collection of web technologies, e.g. HTML, HTTP,
JS, CSS.  I do not want documentation that doubles as manifesto.  I
want new syntax to help where it makes sense, but I want access to
everything as data.

Existing strengths of a language and its tooling should be exploited,
not subverted.

Eventually, asking and answering the basic questions becomes more
important that saving some typing.  Further, subsequently being able
to affect global as well as locally isolated transformations become
more important than hiding the details of the underlying web
technologies.

HOW?

What combination of compile/runtime state plus static analysis do you
use to answer these so-called "basic questions"?  How much do you
encode them as fundamental language elements?  I.e. do you encode
page-linking as module inclusion?  Function callsites?

Well, once you're in the static analysis game, things get harder or at
least there are more questions to answer, e.g. syntax/parsing.
Compile-time state seems to make the most sense; compile into more
primitive language elements, either keeping state whilst doing so or
by emitting runtime elements that do it later.

Is it possible to define single-page and server-round-trip transitions
with the same description?

DATASTORE ISSUES

Schema: Data types, constraints, object graph
Pagination
Update model wrt consistency, aka transactions, phantom reads
Querying with lazy object graph retrieval
  Ordering - define sort functions that closely map
  Caching - marking dirty

Protocol shouldn't matter, i.e. REST, TCP+SQL should not effect many
of the above issues.  Schema and query language are closely related
and cannot, in general, be made uniform without local data plus shim
logic as oppose to direct representation in the remote store.

RENDERING

By default, provide namespaced class and id attributes that can be
compressed along with the corresponding source map for debugging.
Perhaps separate the "functional markup" from the "aesthetic markup"
by providing constraints/transformations distinct from the
template-style approach of views:

(view (form action [input ...]
        (field ...))
      (layout two-column (labels left-justified)))

You should describe the kind of visual object, its role, and the
layout; a rendering pass should transform the "functional markup" in
such a way that the CSS/JS mechanisms used to achieve the desired
effect aren't specified, but derived.  The kind, role, and state
should help determine color, font, etc.  There will always be the need
for overrides.
