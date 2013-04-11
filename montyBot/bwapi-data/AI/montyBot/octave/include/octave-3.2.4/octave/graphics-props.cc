// DO NOT EDIT!  Generated automatically by genprops.awk.

// ******** base ********

base_properties::base_properties (const std::string& ty, const graphics_handle& mh, const graphics_handle& p)
  :     beingdeleted ("beingdeleted", mh, "off"),
    busyaction ("busyaction", mh, "{queue}|cancel"),
    buttondownfcn ("buttondownfcn", mh, Matrix ()),
    children (Matrix ()),
    clipping ("clipping", mh, "on"),
    createfcn ("createfcn", mh, Matrix ()),
    deletefcn ("deletefcn", mh, Matrix ()),
    handlevisibility ("handlevisibility", mh, "{on}|callback|off"),
    hittest ("hittest", mh, "on"),
    interruptible ("interruptible", mh, "on"),
    parent ("parent", mh, p),
    selected ("selected", mh, "off"),
    selectionhighlight ("selectionhighlight", mh, "on"),
    tag ("tag", mh, ""),
    type ("type", mh, ty),
    userdata ("userdata", mh, Matrix ()),
    visible ("visible", mh, "on"),
    __modified__ ("__modified__", mh, "on"),
    __myhandle__ (mh),
    uicontextmenu ("uicontextmenu", mh, graphics_handle ())
{
  beingdeleted.set_id (BEINGDELETED);
  busyaction.set_id (BUSYACTION);
  buttondownfcn.set_id (BUTTONDOWNFCN);
  clipping.set_id (CLIPPING);
  createfcn.set_id (CREATEFCN);
  deletefcn.set_id (DELETEFCN);
  handlevisibility.set_id (HANDLEVISIBILITY);
  hittest.set_id (HITTEST);
  interruptible.set_id (INTERRUPTIBLE);
  parent.set_id (PARENT);
  selected.set_id (SELECTED);
  selectionhighlight.set_id (SELECTIONHIGHLIGHT);
  tag.set_id (TAG);
  type.set_id (TYPE);
  userdata.set_id (USERDATA);
  visible.set_id (VISIBLE);
  __modified__.set_id (__MODIFIED__);
  uicontextmenu.set_id (UICONTEXTMENU);
  init ();
}

void
base_properties::set (const caseless_str& pname, const std::string& cname, const octave_value& val)
{
  if (pname.compare ("beingdeleted"))
    set_beingdeleted (val);
  else if (pname.compare ("busyaction"))
    set_busyaction (val);
  else if (pname.compare ("buttondownfcn"))
    set_buttondownfcn (val);
  else if (pname.compare ("children"))
    set_children (val);
  else if (pname.compare ("clipping"))
    set_clipping (val);
  else if (pname.compare ("createfcn"))
    set_createfcn (val);
  else if (pname.compare ("deletefcn"))
    set_deletefcn (val);
  else if (pname.compare ("handlevisibility"))
    set_handlevisibility (val);
  else if (pname.compare ("hittest"))
    set_hittest (val);
  else if (pname.compare ("interruptible"))
    set_interruptible (val);
  else if (pname.compare ("parent"))
    set_parent (val);
  else if (pname.compare ("selected"))
    set_selected (val);
  else if (pname.compare ("selectionhighlight"))
    set_selectionhighlight (val);
  else if (pname.compare ("tag"))
    set_tag (val);
  else if (pname.compare ("userdata"))
    set_userdata (val);
  else if (pname.compare ("visible"))
    set_visible (val);
  else if (pname.compare ("__modified__"))
    set___modified__ (val);
  else if (pname.compare ("uicontextmenu"))
    set_uicontextmenu (val);
  else
    set_dynamic (pname, cname, val);
}

octave_value
base_properties::get (bool all) const
{
  Octave_map m = get_dynamic (all).map_value ();

  m.assign ("beingdeleted", get_beingdeleted ());
  m.assign ("busyaction", get_busyaction ());
  m.assign ("buttondownfcn", get_buttondownfcn ());
  m.assign ("children", get_children ());
  m.assign ("clipping", get_clipping ());
  m.assign ("createfcn", get_createfcn ());
  m.assign ("deletefcn", get_deletefcn ());
  m.assign ("handlevisibility", get_handlevisibility ());
  m.assign ("hittest", get_hittest ());
  m.assign ("interruptible", get_interruptible ());
  m.assign ("parent", get_parent ().as_octave_value ());
  m.assign ("selected", get_selected ());
  m.assign ("selectionhighlight", get_selectionhighlight ());
  m.assign ("tag", get_tag ());
  m.assign ("type", get_type ());
  m.assign ("userdata", get_userdata ());
  m.assign ("visible", get_visible ());
  m.assign ("__modified__", get___modified__ ());
  if (all)
    m.assign ("__myhandle__", get___myhandle__ ().as_octave_value ());
  m.assign ("uicontextmenu", get_uicontextmenu ().as_octave_value ());

  return m;
}

octave_value
base_properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("beingdeleted"))
    retval = get_beingdeleted ();
  else if (pname.compare ("busyaction"))
    retval = get_busyaction ();
  else if (pname.compare ("buttondownfcn"))
    retval = get_buttondownfcn ();
  else if (pname.compare ("children"))
    retval = get_children ();
  else if (pname.compare ("clipping"))
    retval = get_clipping ();
  else if (pname.compare ("createfcn"))
    retval = get_createfcn ();
  else if (pname.compare ("deletefcn"))
    retval = get_deletefcn ();
  else if (pname.compare ("handlevisibility"))
    retval = get_handlevisibility ();
  else if (pname.compare ("hittest"))
    retval = get_hittest ();
  else if (pname.compare ("interruptible"))
    retval = get_interruptible ();
  else if (pname.compare ("parent"))
    retval = get_parent ().as_octave_value ();
  else if (pname.compare ("selected"))
    retval = get_selected ();
  else if (pname.compare ("selectionhighlight"))
    retval = get_selectionhighlight ();
  else if (pname.compare ("tag"))
    retval = get_tag ();
  else if (pname.compare ("type"))
    retval = get_type ();
  else if (pname.compare ("userdata"))
    retval = get_userdata ();
  else if (pname.compare ("visible"))
    retval = get_visible ();
  else if (pname.compare ("__modified__"))
    retval = get___modified__ ();
  else if (pname.compare ("__myhandle__"))
    retval = get___myhandle__ ().as_octave_value ();
  else if (pname.compare ("uicontextmenu"))
    retval = get_uicontextmenu ().as_octave_value ();
  else
    retval = get_dynamic (pname);

  return retval;
}

property
base_properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("beingdeleted"))
    return property (&beingdeleted, true);
  else if (pname.compare ("busyaction"))
    return property (&busyaction, true);
  else if (pname.compare ("buttondownfcn"))
    return property (&buttondownfcn, true);
  else if (pname.compare ("clipping"))
    return property (&clipping, true);
  else if (pname.compare ("createfcn"))
    return property (&createfcn, true);
  else if (pname.compare ("deletefcn"))
    return property (&deletefcn, true);
  else if (pname.compare ("handlevisibility"))
    return property (&handlevisibility, true);
  else if (pname.compare ("hittest"))
    return property (&hittest, true);
  else if (pname.compare ("interruptible"))
    return property (&interruptible, true);
  else if (pname.compare ("parent"))
    return property (&parent, true);
  else if (pname.compare ("selected"))
    return property (&selected, true);
  else if (pname.compare ("selectionhighlight"))
    return property (&selectionhighlight, true);
  else if (pname.compare ("tag"))
    return property (&tag, true);
  else if (pname.compare ("type"))
    return property (&type, true);
  else if (pname.compare ("userdata"))
    return property (&userdata, true);
  else if (pname.compare ("visible"))
    return property (&visible, true);
  else if (pname.compare ("__modified__"))
    return property (&__modified__, true);
  else if (pname.compare ("uicontextmenu"))
    return property (&uicontextmenu, true);
  else
    return get_property_dynamic (pname);
}

property_list::pval_map_type
base_properties::factory_defaults (void)
{
  property_list::pval_map_type m;

  m["beingdeleted"] = "off";
  m["busyaction"] = "queue";
  m["buttondownfcn"] = Matrix ();
  m["clipping"] = "on";
  m["createfcn"] = Matrix ();
  m["deletefcn"] = Matrix ();
  m["handlevisibility"] = "on";
  m["hittest"] = "on";
  m["interruptible"] = "on";
  m["selected"] = "off";
  m["selectionhighlight"] = "on";
  m["tag"] = "";
  m["userdata"] = Matrix ();
  m["visible"] = "on";
  m["__modified__"] = "on";
  m["uicontextmenu"] = graphics_handle ().as_octave_value ();

  return m;
}

bool base_properties::has_property (const std::string& pname, const std::string& cname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("beingdeleted");
      all_properties.insert ("busyaction");
      all_properties.insert ("buttondownfcn");
      all_properties.insert ("children");
      all_properties.insert ("clipping");
      all_properties.insert ("createfcn");
      all_properties.insert ("deletefcn");
      all_properties.insert ("handlevisibility");
      all_properties.insert ("hittest");
      all_properties.insert ("interruptible");
      all_properties.insert ("parent");
      all_properties.insert ("selected");
      all_properties.insert ("selectionhighlight");
      all_properties.insert ("tag");
      all_properties.insert ("type");
      all_properties.insert ("userdata");
      all_properties.insert ("visible");
      all_properties.insert ("__modified__");
      all_properties.insert ("__myhandle__");
      all_properties.insert ("uicontextmenu");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || has_dynamic_property (pname, cname);
}

// ******** root_figure ********

root_figure::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    currentfigure ("currentfigure", mh, graphics_handle ()),
    callbackobject ("callbackobject", mh, graphics_handle ()),
    screendepth ("screendepth", mh, default_screendepth ()),
    screensize ("screensize", mh, default_screensize ()),
    screenpixelsperinch ("screenpixelsperinch", mh, default_screenpixelsperinch ()),
    units ("units", mh, "inches|centimeters|normalized|points|{pixels}"),
    showhiddenhandles ("showhiddenhandles", mh, "off")
{
  currentfigure.set_id (CURRENTFIGURE);
  callbackobject.set_id (CALLBACKOBJECT);
  screendepth.set_id (SCREENDEPTH);
  screensize.set_id (SCREENSIZE);
  screenpixelsperinch.set_id (SCREENPIXELSPERINCH);
  units.set_id (UNITS);
  showhiddenhandles.set_id (SHOWHIDDENHANDLES);
  init ();
}

void
root_figure::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("currentfigure"))
    set_currentfigure (val);
  else if (pname.compare ("units"))
    set_units (val);
  else if (pname.compare ("showhiddenhandles"))
    set_showhiddenhandles (val);
  else
    base_properties::set (pname, "root_figure", val);
}

octave_value
root_figure::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("currentfigure", get_currentfigure ().as_octave_value ());
  m.assign ("callbackobject", get_callbackobject ().as_octave_value ());
  m.assign ("screendepth", get_screendepth ());
  m.assign ("screensize", get_screensize ());
  m.assign ("screenpixelsperinch", get_screenpixelsperinch ());
  m.assign ("units", get_units ());
  m.assign ("showhiddenhandles", get_showhiddenhandles ());

  return m;
}

octave_value
root_figure::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("currentfigure"))
    retval = get_currentfigure ().as_octave_value ();
  else if (pname.compare ("callbackobject"))
    retval = get_callbackobject ().as_octave_value ();
  else if (pname.compare ("screendepth"))
    retval = get_screendepth ();
  else if (pname.compare ("screensize"))
    retval = get_screensize ();
  else if (pname.compare ("screenpixelsperinch"))
    retval = get_screenpixelsperinch ();
  else if (pname.compare ("units"))
    retval = get_units ();
  else if (pname.compare ("showhiddenhandles"))
    retval = get_showhiddenhandles ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
root_figure::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("currentfigure"))
    return property (&currentfigure, true);
  else if (pname.compare ("callbackobject"))
    return property (&callbackobject, true);
  else if (pname.compare ("screendepth"))
    return property (&screendepth, true);
  else if (pname.compare ("screensize"))
    return property (&screensize, true);
  else if (pname.compare ("screenpixelsperinch"))
    return property (&screenpixelsperinch, true);
  else if (pname.compare ("units"))
    return property (&units, true);
  else if (pname.compare ("showhiddenhandles"))
    return property (&showhiddenhandles, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
root_figure::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["currentfigure"] = graphics_handle ().as_octave_value ();
  m["callbackobject"] = graphics_handle ().as_octave_value ();
  m["screendepth"] = default_screendepth ();
  m["screensize"] = default_screensize ();
  m["screenpixelsperinch"] = default_screenpixelsperinch ();
  m["units"] = "pixels";
  m["showhiddenhandles"] = "off";

  return m;
}

std::string root_figure::properties::go_name ("root");

bool root_figure::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("currentfigure");
      all_properties.insert ("callbackobject");
      all_properties.insert ("screendepth");
      all_properties.insert ("screensize");
      all_properties.insert ("screenpixelsperinch");
      all_properties.insert ("units");
      all_properties.insert ("showhiddenhandles");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "root_figure");
}

// ******** figure ********

figure::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    __plot_stream__ ("__plot_stream__", mh, Matrix ()),
    __enhanced__ ("__enhanced__", mh, "on"),
    nextplot ("nextplot", mh, "new|{add}|replace_children|replace"),
    closerequestfcn ("closerequestfcn", mh, "closereq"),
    currentaxes ("currentaxes", mh, graphics_handle ()),
    colormap ("colormap", mh, jet_colormap ()),
    paperorientation ("paperorientation", mh, "{portrait}|landscape|rotated"),
    color ("color", mh, color_values (1, 1, 1)),
    alphamap ("alphamap", mh, Matrix (64, 1, 1)),
    currentcharacter ("currentcharacter", mh, ""),
    currentobject ("currentobject", mh, graphics_handle ()),
    current_point ("current_point", mh, Matrix (2, 1, 0)),
    dockcontrols ("dockcontrols", mh, "off"),
    doublebuffer ("doublebuffer", mh, "on"),
    filename ("filename", mh, ""),
    integerhandle ("integerhandle", mh, "on"),
    inverthardcopy ("inverthardcopy", mh, "off"),
    keypressfcn ("keypressfcn", mh, Matrix ()),
    keyreleasefcn ("keyreleasefcn", mh, Matrix ()),
    menubar ("menubar", mh, "none|{figure}"),
    mincolormap ("mincolormap", mh, 64),
    name ("name", mh, ""),
    numbertitle ("numbertitle", mh, "on"),
    paperunits ("paperunits", mh, "{inches}|centimeters|normalized|points"),
    paperposition ("paperposition", mh, default_figure_paperposition ()),
    paperpositionmode ("paperpositionmode", mh, "auto|{manual}"),
    papersize ("papersize", mh, default_figure_papersize ()),
    papertype ("papertype", mh, "{usletter}|uslegal|a0|a1|a2|a3|a4|a5|b0|b1|b2|b3|b4|b5|arch-a|arch-b|arch-c|arch-d|arch-e|a|b|c|d|e|tabloid|<custom>"),
    pointer ("pointer", mh, "crosshair|fullcrosshair|{arrow}|ibeam|watch|topl|topr|botl|botr|left|top|right|bottom|circle|cross|fleur|custom|hand"),
    pointershapecdata ("pointershapecdata", mh, Matrix (16, 16, 0)),
    pointershapehotspot ("pointershapehotspot", mh, Matrix (1, 2, 0)),
    position ("position", mh, default_figure_position ()),
    renderer ("renderer", mh, "{painters}|zbuffer|opengl|none"),
    renderermode ("renderermode", mh, "{auto}|manual"),
    resize ("resize", mh, "on"),
    resizefcn ("resizefcn", mh, Matrix ()),
    selectiontype ("selectiontype", mh, "{normal}|open|alt|extend"),
    toolbar ("toolbar", mh, "none|{auto}|figure"),
    units ("units", mh, "inches|centimeters|normalized|points|{pixels}|characters"),
    windowbuttondownfcn ("windowbuttondownfcn", mh, Matrix ()),
    windowbuttonmotionfcn ("windowbuttonmotionfcn", mh, Matrix ()),
    windowbuttonupfcn ("windowbuttonupfcn", mh, Matrix ()),
    windowbuttonwheelfcn ("windowbuttonwheelfcn", mh, Matrix ()),
    windowstyle ("windowstyle", mh, "{normal}|modal|docked"),
    wvisual ("wvisual", mh, ""),
    wvisualmode ("wvisualmode", mh, "{auto}|manual"),
    xdisplay ("xdisplay", mh, ""),
    xvisual ("xvisual", mh, ""),
    xvisualmode ("xvisualmode", mh, "{auto}|manual"),
    buttondownfcn ("buttondownfcn", mh, Matrix ()),
    __backend__ ("__backend__", mh, "gnuplot")
{
  __plot_stream__.set_id (__PLOT_STREAM__);
  __plot_stream__.set_hidden (true);
  __enhanced__.set_id (__ENHANCED__);
  __enhanced__.set_hidden (true);
  nextplot.set_id (NEXTPLOT);
  closerequestfcn.set_id (CLOSEREQUESTFCN);
  currentaxes.set_id (CURRENTAXES);
  colormap.set_id (COLORMAP);
  paperorientation.set_id (PAPERORIENTATION);
  color.set_id (COLOR);
  alphamap.set_id (ALPHAMAP);
  currentcharacter.set_id (CURRENTCHARACTER);
  currentobject.set_id (CURRENTOBJECT);
  current_point.set_id (CURRENT_POINT);
  dockcontrols.set_id (DOCKCONTROLS);
  doublebuffer.set_id (DOUBLEBUFFER);
  filename.set_id (FILENAME);
  integerhandle.set_id (INTEGERHANDLE);
  inverthardcopy.set_id (INVERTHARDCOPY);
  keypressfcn.set_id (KEYPRESSFCN);
  keyreleasefcn.set_id (KEYRELEASEFCN);
  menubar.set_id (MENUBAR);
  mincolormap.set_id (MINCOLORMAP);
  name.set_id (NAME);
  numbertitle.set_id (NUMBERTITLE);
  paperunits.set_id (PAPERUNITS);
  paperposition.set_id (PAPERPOSITION);
  paperpositionmode.set_id (PAPERPOSITIONMODE);
  papersize.set_id (PAPERSIZE);
  papertype.set_id (PAPERTYPE);
  pointer.set_id (POINTER);
  pointershapecdata.set_id (POINTERSHAPECDATA);
  pointershapehotspot.set_id (POINTERSHAPEHOTSPOT);
  position.set_id (POSITION);
  renderer.set_id (RENDERER);
  renderermode.set_id (RENDERERMODE);
  resize.set_id (RESIZE);
  resizefcn.set_id (RESIZEFCN);
  selectiontype.set_id (SELECTIONTYPE);
  toolbar.set_id (TOOLBAR);
  units.set_id (UNITS);
  windowbuttondownfcn.set_id (WINDOWBUTTONDOWNFCN);
  windowbuttonmotionfcn.set_id (WINDOWBUTTONMOTIONFCN);
  windowbuttonupfcn.set_id (WINDOWBUTTONUPFCN);
  windowbuttonwheelfcn.set_id (WINDOWBUTTONWHEELFCN);
  windowstyle.set_id (WINDOWSTYLE);
  wvisual.set_id (WVISUAL);
  wvisualmode.set_id (WVISUALMODE);
  xdisplay.set_id (XDISPLAY);
  xvisual.set_id (XVISUAL);
  xvisualmode.set_id (XVISUALMODE);
  buttondownfcn.set_id (BUTTONDOWNFCN);
  __backend__.set_id (__BACKEND__);
  init ();
}

void
figure::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("__plot_stream__"))
    set___plot_stream__ (val);
  else if (pname.compare ("__enhanced__"))
    set___enhanced__ (val);
  else if (pname.compare ("nextplot"))
    set_nextplot (val);
  else if (pname.compare ("closerequestfcn"))
    set_closerequestfcn (val);
  else if (pname.compare ("currentaxes"))
    set_currentaxes (val);
  else if (pname.compare ("colormap"))
    set_colormap (val);
  else if (pname.compare ("paperorientation"))
    set_paperorientation (val);
  else if (pname.compare ("color"))
    set_color (val);
  else if (pname.compare ("alphamap"))
    set_alphamap (val);
  else if (pname.compare ("dockcontrols"))
    set_dockcontrols (val);
  else if (pname.compare ("doublebuffer"))
    set_doublebuffer (val);
  else if (pname.compare ("integerhandle"))
    set_integerhandle (val);
  else if (pname.compare ("inverthardcopy"))
    set_inverthardcopy (val);
  else if (pname.compare ("keypressfcn"))
    set_keypressfcn (val);
  else if (pname.compare ("keyreleasefcn"))
    set_keyreleasefcn (val);
  else if (pname.compare ("menubar"))
    set_menubar (val);
  else if (pname.compare ("mincolormap"))
    set_mincolormap (val);
  else if (pname.compare ("name"))
    set_name (val);
  else if (pname.compare ("numbertitle"))
    set_numbertitle (val);
  else if (pname.compare ("paperunits"))
    set_paperunits (val);
  else if (pname.compare ("paperposition"))
    set_paperposition (val);
  else if (pname.compare ("paperpositionmode"))
    set_paperpositionmode (val);
  else if (pname.compare ("papersize"))
    set_papersize (val);
  else if (pname.compare ("papertype"))
    set_papertype (val);
  else if (pname.compare ("pointer"))
    set_pointer (val);
  else if (pname.compare ("pointershapecdata"))
    set_pointershapecdata (val);
  else if (pname.compare ("pointershapehotspot"))
    set_pointershapehotspot (val);
  else if (pname.compare ("position"))
    set_position (val);
  else if (pname.compare ("renderer"))
    set_renderer (val);
  else if (pname.compare ("renderermode"))
    set_renderermode (val);
  else if (pname.compare ("resize"))
    set_resize (val);
  else if (pname.compare ("resizefcn"))
    set_resizefcn (val);
  else if (pname.compare ("selectiontype"))
    set_selectiontype (val);
  else if (pname.compare ("toolbar"))
    set_toolbar (val);
  else if (pname.compare ("units"))
    set_units (val);
  else if (pname.compare ("windowbuttondownfcn"))
    set_windowbuttondownfcn (val);
  else if (pname.compare ("windowbuttonmotionfcn"))
    set_windowbuttonmotionfcn (val);
  else if (pname.compare ("windowbuttonupfcn"))
    set_windowbuttonupfcn (val);
  else if (pname.compare ("windowbuttonwheelfcn"))
    set_windowbuttonwheelfcn (val);
  else if (pname.compare ("windowstyle"))
    set_windowstyle (val);
  else if (pname.compare ("wvisual"))
    set_wvisual (val);
  else if (pname.compare ("wvisualmode"))
    set_wvisualmode (val);
  else if (pname.compare ("xdisplay"))
    set_xdisplay (val);
  else if (pname.compare ("xvisual"))
    set_xvisual (val);
  else if (pname.compare ("xvisualmode"))
    set_xvisualmode (val);
  else if (pname.compare ("buttondownfcn"))
    set_buttondownfcn (val);
  else if (pname.compare ("__backend__"))
    set___backend__ (val);
  else
    base_properties::set (pname, "figure", val);
}

octave_value
figure::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  if (all)
    m.assign ("__plot_stream__", get___plot_stream__ ());
  if (all)
    m.assign ("__enhanced__", get___enhanced__ ());
  m.assign ("nextplot", get_nextplot ());
  m.assign ("closerequestfcn", get_closerequestfcn ());
  m.assign ("currentaxes", get_currentaxes ().as_octave_value ());
  m.assign ("colormap", get_colormap ());
  m.assign ("paperorientation", get_paperorientation ());
  m.assign ("color", get_color ());
  m.assign ("alphamap", get_alphamap ());
  m.assign ("currentcharacter", get_currentcharacter ());
  m.assign ("currentobject", get_currentobject ().as_octave_value ());
  m.assign ("current_point", get_current_point ());
  m.assign ("dockcontrols", get_dockcontrols ());
  m.assign ("doublebuffer", get_doublebuffer ());
  m.assign ("filename", get_filename ());
  m.assign ("integerhandle", get_integerhandle ());
  m.assign ("inverthardcopy", get_inverthardcopy ());
  m.assign ("keypressfcn", get_keypressfcn ());
  m.assign ("keyreleasefcn", get_keyreleasefcn ());
  m.assign ("menubar", get_menubar ());
  m.assign ("mincolormap", get_mincolormap ());
  m.assign ("name", get_name ());
  m.assign ("numbertitle", get_numbertitle ());
  m.assign ("paperunits", get_paperunits ());
  m.assign ("paperposition", get_paperposition ());
  m.assign ("paperpositionmode", get_paperpositionmode ());
  m.assign ("papersize", get_papersize ());
  m.assign ("papertype", get_papertype ());
  m.assign ("pointer", get_pointer ());
  m.assign ("pointershapecdata", get_pointershapecdata ());
  m.assign ("pointershapehotspot", get_pointershapehotspot ());
  m.assign ("position", get_position ());
  m.assign ("renderer", get_renderer ());
  m.assign ("renderermode", get_renderermode ());
  m.assign ("resize", get_resize ());
  m.assign ("resizefcn", get_resizefcn ());
  m.assign ("selectiontype", get_selectiontype ());
  m.assign ("toolbar", get_toolbar ());
  m.assign ("units", get_units ());
  m.assign ("windowbuttondownfcn", get_windowbuttondownfcn ());
  m.assign ("windowbuttonmotionfcn", get_windowbuttonmotionfcn ());
  m.assign ("windowbuttonupfcn", get_windowbuttonupfcn ());
  m.assign ("windowbuttonwheelfcn", get_windowbuttonwheelfcn ());
  m.assign ("windowstyle", get_windowstyle ());
  m.assign ("wvisual", get_wvisual ());
  m.assign ("wvisualmode", get_wvisualmode ());
  m.assign ("xdisplay", get_xdisplay ());
  m.assign ("xvisual", get_xvisual ());
  m.assign ("xvisualmode", get_xvisualmode ());
  m.assign ("buttondownfcn", get_buttondownfcn ());
  m.assign ("__backend__", get___backend__ ());

  return m;
}

octave_value
figure::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("__plot_stream__"))
    retval = get___plot_stream__ ();
  else if (pname.compare ("__enhanced__"))
    retval = get___enhanced__ ();
  else if (pname.compare ("nextplot"))
    retval = get_nextplot ();
  else if (pname.compare ("closerequestfcn"))
    retval = get_closerequestfcn ();
  else if (pname.compare ("currentaxes"))
    retval = get_currentaxes ().as_octave_value ();
  else if (pname.compare ("colormap"))
    retval = get_colormap ();
  else if (pname.compare ("paperorientation"))
    retval = get_paperorientation ();
  else if (pname.compare ("color"))
    retval = get_color ();
  else if (pname.compare ("alphamap"))
    retval = get_alphamap ();
  else if (pname.compare ("currentcharacter"))
    retval = get_currentcharacter ();
  else if (pname.compare ("currentobject"))
    retval = get_currentobject ().as_octave_value ();
  else if (pname.compare ("current_point"))
    retval = get_current_point ();
  else if (pname.compare ("dockcontrols"))
    retval = get_dockcontrols ();
  else if (pname.compare ("doublebuffer"))
    retval = get_doublebuffer ();
  else if (pname.compare ("filename"))
    retval = get_filename ();
  else if (pname.compare ("integerhandle"))
    retval = get_integerhandle ();
  else if (pname.compare ("inverthardcopy"))
    retval = get_inverthardcopy ();
  else if (pname.compare ("keypressfcn"))
    retval = get_keypressfcn ();
  else if (pname.compare ("keyreleasefcn"))
    retval = get_keyreleasefcn ();
  else if (pname.compare ("menubar"))
    retval = get_menubar ();
  else if (pname.compare ("mincolormap"))
    retval = get_mincolormap ();
  else if (pname.compare ("name"))
    retval = get_name ();
  else if (pname.compare ("numbertitle"))
    retval = get_numbertitle ();
  else if (pname.compare ("paperunits"))
    retval = get_paperunits ();
  else if (pname.compare ("paperposition"))
    retval = get_paperposition ();
  else if (pname.compare ("paperpositionmode"))
    retval = get_paperpositionmode ();
  else if (pname.compare ("papersize"))
    retval = get_papersize ();
  else if (pname.compare ("papertype"))
    retval = get_papertype ();
  else if (pname.compare ("pointer"))
    retval = get_pointer ();
  else if (pname.compare ("pointershapecdata"))
    retval = get_pointershapecdata ();
  else if (pname.compare ("pointershapehotspot"))
    retval = get_pointershapehotspot ();
  else if (pname.compare ("position"))
    retval = get_position ();
  else if (pname.compare ("renderer"))
    retval = get_renderer ();
  else if (pname.compare ("renderermode"))
    retval = get_renderermode ();
  else if (pname.compare ("resize"))
    retval = get_resize ();
  else if (pname.compare ("resizefcn"))
    retval = get_resizefcn ();
  else if (pname.compare ("selectiontype"))
    retval = get_selectiontype ();
  else if (pname.compare ("toolbar"))
    retval = get_toolbar ();
  else if (pname.compare ("units"))
    retval = get_units ();
  else if (pname.compare ("windowbuttondownfcn"))
    retval = get_windowbuttondownfcn ();
  else if (pname.compare ("windowbuttonmotionfcn"))
    retval = get_windowbuttonmotionfcn ();
  else if (pname.compare ("windowbuttonupfcn"))
    retval = get_windowbuttonupfcn ();
  else if (pname.compare ("windowbuttonwheelfcn"))
    retval = get_windowbuttonwheelfcn ();
  else if (pname.compare ("windowstyle"))
    retval = get_windowstyle ();
  else if (pname.compare ("wvisual"))
    retval = get_wvisual ();
  else if (pname.compare ("wvisualmode"))
    retval = get_wvisualmode ();
  else if (pname.compare ("xdisplay"))
    retval = get_xdisplay ();
  else if (pname.compare ("xvisual"))
    retval = get_xvisual ();
  else if (pname.compare ("xvisualmode"))
    retval = get_xvisualmode ();
  else if (pname.compare ("buttondownfcn"))
    retval = get_buttondownfcn ();
  else if (pname.compare ("__backend__"))
    retval = get___backend__ ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
figure::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("__plot_stream__"))
    return property (&__plot_stream__, true);
  else if (pname.compare ("__enhanced__"))
    return property (&__enhanced__, true);
  else if (pname.compare ("nextplot"))
    return property (&nextplot, true);
  else if (pname.compare ("closerequestfcn"))
    return property (&closerequestfcn, true);
  else if (pname.compare ("currentaxes"))
    return property (&currentaxes, true);
  else if (pname.compare ("colormap"))
    return property (&colormap, true);
  else if (pname.compare ("paperorientation"))
    return property (&paperorientation, true);
  else if (pname.compare ("color"))
    return property (&color, true);
  else if (pname.compare ("alphamap"))
    return property (&alphamap, true);
  else if (pname.compare ("currentcharacter"))
    return property (&currentcharacter, true);
  else if (pname.compare ("currentobject"))
    return property (&currentobject, true);
  else if (pname.compare ("current_point"))
    return property (&current_point, true);
  else if (pname.compare ("dockcontrols"))
    return property (&dockcontrols, true);
  else if (pname.compare ("doublebuffer"))
    return property (&doublebuffer, true);
  else if (pname.compare ("filename"))
    return property (&filename, true);
  else if (pname.compare ("integerhandle"))
    return property (&integerhandle, true);
  else if (pname.compare ("inverthardcopy"))
    return property (&inverthardcopy, true);
  else if (pname.compare ("keypressfcn"))
    return property (&keypressfcn, true);
  else if (pname.compare ("keyreleasefcn"))
    return property (&keyreleasefcn, true);
  else if (pname.compare ("menubar"))
    return property (&menubar, true);
  else if (pname.compare ("mincolormap"))
    return property (&mincolormap, true);
  else if (pname.compare ("name"))
    return property (&name, true);
  else if (pname.compare ("numbertitle"))
    return property (&numbertitle, true);
  else if (pname.compare ("paperunits"))
    return property (&paperunits, true);
  else if (pname.compare ("paperposition"))
    return property (&paperposition, true);
  else if (pname.compare ("paperpositionmode"))
    return property (&paperpositionmode, true);
  else if (pname.compare ("papersize"))
    return property (&papersize, true);
  else if (pname.compare ("papertype"))
    return property (&papertype, true);
  else if (pname.compare ("pointer"))
    return property (&pointer, true);
  else if (pname.compare ("pointershapecdata"))
    return property (&pointershapecdata, true);
  else if (pname.compare ("pointershapehotspot"))
    return property (&pointershapehotspot, true);
  else if (pname.compare ("position"))
    return property (&position, true);
  else if (pname.compare ("renderer"))
    return property (&renderer, true);
  else if (pname.compare ("renderermode"))
    return property (&renderermode, true);
  else if (pname.compare ("resize"))
    return property (&resize, true);
  else if (pname.compare ("resizefcn"))
    return property (&resizefcn, true);
  else if (pname.compare ("selectiontype"))
    return property (&selectiontype, true);
  else if (pname.compare ("toolbar"))
    return property (&toolbar, true);
  else if (pname.compare ("units"))
    return property (&units, true);
  else if (pname.compare ("windowbuttondownfcn"))
    return property (&windowbuttondownfcn, true);
  else if (pname.compare ("windowbuttonmotionfcn"))
    return property (&windowbuttonmotionfcn, true);
  else if (pname.compare ("windowbuttonupfcn"))
    return property (&windowbuttonupfcn, true);
  else if (pname.compare ("windowbuttonwheelfcn"))
    return property (&windowbuttonwheelfcn, true);
  else if (pname.compare ("windowstyle"))
    return property (&windowstyle, true);
  else if (pname.compare ("wvisual"))
    return property (&wvisual, true);
  else if (pname.compare ("wvisualmode"))
    return property (&wvisualmode, true);
  else if (pname.compare ("xdisplay"))
    return property (&xdisplay, true);
  else if (pname.compare ("xvisual"))
    return property (&xvisual, true);
  else if (pname.compare ("xvisualmode"))
    return property (&xvisualmode, true);
  else if (pname.compare ("buttondownfcn"))
    return property (&buttondownfcn, true);
  else if (pname.compare ("__backend__"))
    return property (&__backend__, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
figure::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["__plot_stream__"] = Matrix ();
  m["__enhanced__"] = "on";
  m["nextplot"] = "add";
  m["closerequestfcn"] = "closereq";
  m["currentaxes"] = graphics_handle ().as_octave_value ();
  m["colormap"] = jet_colormap ();
  m["paperorientation"] = "portrait";
  m["color"] = octave_value ();
  m["alphamap"] = Matrix (64, 1, 1);
  m["currentcharacter"] = "";
  m["currentobject"] = graphics_handle ().as_octave_value ();
  m["current_point"] = Matrix (2, 1, 0);
  m["dockcontrols"] = "off";
  m["doublebuffer"] = "on";
  m["filename"] = "";
  m["integerhandle"] = "on";
  m["inverthardcopy"] = "off";
  m["keypressfcn"] = Matrix ();
  m["keyreleasefcn"] = Matrix ();
  m["menubar"] = "figure";
  m["mincolormap"] = 64;
  m["name"] = "";
  m["numbertitle"] = "on";
  m["paperunits"] = "inches";
  m["paperposition"] = default_figure_paperposition ();
  m["paperpositionmode"] = "manual";
  m["papersize"] = default_figure_papersize ();
  m["papertype"] = "usletter";
  m["pointer"] = "arrow";
  m["pointershapecdata"] = Matrix (16, 16, 0);
  m["pointershapehotspot"] = Matrix (1, 2, 0);
  m["position"] = default_figure_position ();
  m["renderer"] = "painters";
  m["renderermode"] = "auto";
  m["resize"] = "on";
  m["resizefcn"] = Matrix ();
  m["selectiontype"] = "normal";
  m["toolbar"] = "auto";
  m["units"] = "pixels";
  m["windowbuttondownfcn"] = Matrix ();
  m["windowbuttonmotionfcn"] = Matrix ();
  m["windowbuttonupfcn"] = Matrix ();
  m["windowbuttonwheelfcn"] = Matrix ();
  m["windowstyle"] = "normal";
  m["wvisual"] = "";
  m["wvisualmode"] = "auto";
  m["xdisplay"] = "";
  m["xvisual"] = "";
  m["xvisualmode"] = "auto";
  m["buttondownfcn"] = Matrix ();
  m["__backend__"] = "gnuplot";

  return m;
}

std::string figure::properties::go_name ("figure");

bool figure::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("__plot_stream__");
      all_properties.insert ("__enhanced__");
      all_properties.insert ("nextplot");
      all_properties.insert ("closerequestfcn");
      all_properties.insert ("currentaxes");
      all_properties.insert ("colormap");
      all_properties.insert ("paperorientation");
      all_properties.insert ("color");
      all_properties.insert ("alphamap");
      all_properties.insert ("currentcharacter");
      all_properties.insert ("currentobject");
      all_properties.insert ("current_point");
      all_properties.insert ("dockcontrols");
      all_properties.insert ("doublebuffer");
      all_properties.insert ("filename");
      all_properties.insert ("integerhandle");
      all_properties.insert ("inverthardcopy");
      all_properties.insert ("keypressfcn");
      all_properties.insert ("keyreleasefcn");
      all_properties.insert ("menubar");
      all_properties.insert ("mincolormap");
      all_properties.insert ("name");
      all_properties.insert ("numbertitle");
      all_properties.insert ("paperunits");
      all_properties.insert ("paperposition");
      all_properties.insert ("paperpositionmode");
      all_properties.insert ("papersize");
      all_properties.insert ("papertype");
      all_properties.insert ("pointer");
      all_properties.insert ("pointershapecdata");
      all_properties.insert ("pointershapehotspot");
      all_properties.insert ("position");
      all_properties.insert ("renderer");
      all_properties.insert ("renderermode");
      all_properties.insert ("resize");
      all_properties.insert ("resizefcn");
      all_properties.insert ("selectiontype");
      all_properties.insert ("toolbar");
      all_properties.insert ("units");
      all_properties.insert ("windowbuttondownfcn");
      all_properties.insert ("windowbuttonmotionfcn");
      all_properties.insert ("windowbuttonupfcn");
      all_properties.insert ("windowbuttonwheelfcn");
      all_properties.insert ("windowstyle");
      all_properties.insert ("wvisual");
      all_properties.insert ("wvisualmode");
      all_properties.insert ("xdisplay");
      all_properties.insert ("xvisual");
      all_properties.insert ("xvisualmode");
      all_properties.insert ("buttondownfcn");
      all_properties.insert ("__backend__");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "figure");
}

// ******** axes ********

axes::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    position ("position", mh, default_axes_position ()),
    box ("box", mh, "on"),
    key ("key", mh, "off"),
    keybox ("keybox", mh, "off"),
    keyreverse ("keyreverse", mh, "off"),
    keypos ("keypos", mh, 1),
    colororder ("colororder", mh, default_colororder ()),
    dataaspectratio ("dataaspectratio", mh, Matrix (1, 3, 1.0)),
    dataaspectratiomode ("dataaspectratiomode", mh, "{auto}|manual"),
    layer ("layer", mh, "{bottom}|top"),
    xlim ("xlim", mh, default_lim ()),
    ylim ("ylim", mh, default_lim ()),
    zlim ("zlim", mh, default_lim ()),
    clim ("clim", mh, default_lim ()),
    alim ("alim", mh, default_lim ()),
    xlimmode ("xlimmode", mh, "{auto}|manual"),
    ylimmode ("ylimmode", mh, "{auto}|manual"),
    zlimmode ("zlimmode", mh, "{auto}|manual"),
    climmode ("climmode", mh, "{auto}|manual"),
    alimmode ("alimmode", mh, "{auto}|manual"),
    xlabel ("xlabel", mh, gh_manager::make_graphics_handle ("text", __myhandle__, false)),
    ylabel ("ylabel", mh, gh_manager::make_graphics_handle ("text", __myhandle__, false)),
    zlabel ("zlabel", mh, gh_manager::make_graphics_handle ("text", __myhandle__, false)),
    title ("title", mh, gh_manager::make_graphics_handle ("text", __myhandle__, false)),
    xgrid ("xgrid", mh, "off"),
    ygrid ("ygrid", mh, "off"),
    zgrid ("zgrid", mh, "off"),
    xminorgrid ("xminorgrid", mh, "off"),
    yminorgrid ("yminorgrid", mh, "off"),
    zminorgrid ("zminorgrid", mh, "off"),
    xtick ("xtick", mh, default_axes_tick ()),
    ytick ("ytick", mh, default_axes_tick ()),
    ztick ("ztick", mh, default_axes_tick ()),
    xtickmode ("xtickmode", mh, "{auto}|manual"),
    ytickmode ("ytickmode", mh, "{auto}|manual"),
    ztickmode ("ztickmode", mh, "{auto}|manual"),
    xminortick ("xminortick", mh, "off"),
    yminortick ("yminortick", mh, "off"),
    zminortick ("zminortick", mh, "off"),
    xticklabel ("xticklabel", mh, ""),
    yticklabel ("yticklabel", mh, ""),
    zticklabel ("zticklabel", mh, ""),
    xticklabelmode ("xticklabelmode", mh, "{auto}|manual"),
    yticklabelmode ("yticklabelmode", mh, "{auto}|manual"),
    zticklabelmode ("zticklabelmode", mh, "{auto}|manual"),
    interpreter ("interpreter", mh, "tex|{none}|latex"),
    color ("color", mh, color_property (color_values (1, 1, 1), radio_values ("none"))),
    xcolor ("xcolor", mh, color_values (0, 0, 0)),
    ycolor ("ycolor", mh, color_values (0, 0, 0)),
    zcolor ("zcolor", mh, color_values (0, 0, 0)),
    xscale ("xscale", mh, "{linear}|log"),
    yscale ("yscale", mh, "{linear}|log"),
    zscale ("zscale", mh, "{linear}|log"),
    xdir ("xdir", mh, "{normal}|reverse"),
    ydir ("ydir", mh, "{normal}|reverse"),
    zdir ("zdir", mh, "{normal}|reverse"),
    yaxislocation ("yaxislocation", mh, "{left}|right|zero"),
    xaxislocation ("xaxislocation", mh, "{bottom}|top|zero"),
    view ("view", mh, Matrix ()),
    nextplot ("nextplot", mh, "add|replace_children|{replace}"),
    outerposition ("outerposition", mh, default_axes_outerposition ()),
    activepositionproperty ("activepositionproperty", mh, "{outerposition}|position"),
    ambientlightcolor ("ambientlightcolor", mh, color_values (1, 1, 1)),
    cameraposition ("cameraposition", mh, Matrix (1, 3, 0.0)),
    cameratarget ("cameratarget", mh, Matrix (1, 3, 0.0)),
    cameraupvector ("cameraupvector", mh, Matrix ()),
    cameraviewangle ("cameraviewangle", mh, 10.0),
    camerapositionmode ("camerapositionmode", mh, "{auto}|manual"),
    cameratargetmode ("cameratargetmode", mh, "{auto}|manual"),
    cameraupvectormode ("cameraupvectormode", mh, "{auto}|manual"),
    cameraviewanglemode ("cameraviewanglemode", mh, "{auto}|manual"),
    currentpoint ("currentpoint", mh, Matrix (2, 3, 0.0)),
    drawmode ("drawmode", mh, "{normal}|fast"),
    fontangle ("fontangle", mh, "{normal}|italic|oblique"),
    fontname ("fontname", mh, OCTAVE_DEFAULT_FONTNAME),
    fontsize ("fontsize", mh, 12),
    fontunits ("fontunits", mh, "{points}|normalized|inches|centimeters|pixels"),
    fontweight ("fontweight", mh, "{normal}|light|demi|bold"),
    gridlinestyle ("gridlinestyle", mh, "-|--|{:}|-.|none"),
    linestyleorder ("linestyleorder", mh, "-"),
    linewidth ("linewidth", mh, 0.5),
    minorgridlinestyle ("minorgridlinestyle", mh, "-|--|{:}|-.|none"),
    plotboxaspectratio ("plotboxaspectratio", mh, Matrix (1, 3, 1.0)),
    plotboxaspectratiomode ("plotboxaspectratiomode", mh, "{auto}|manual"),
    projection ("projection", mh, "{orthographic}|perpective"),
    tickdir ("tickdir", mh, "{in}|out"),
    tickdirmode ("tickdirmode", mh, "{auto}|manual"),
    ticklength ("ticklength", mh, default_axes_ticklength ()),
    tightinset ("tightinset", mh, Matrix (1, 4, 0.0)),
    units ("units", mh, "{normalized}|inches|centimeters|points|pixels|characters"),
    x_viewtransform ("x_viewtransform", mh, Matrix (4, 4, 0.0)),
    x_projectiontransform ("x_projectiontransform", mh, Matrix (4, 4, 0.0)),
    x_viewporttransform ("x_viewporttransform", mh, Matrix (4, 4, 0.0)),
    x_normrendertransform ("x_normrendertransform", mh, Matrix (4, 4, 0.0)),
    x_rendertransform ("x_rendertransform", mh, Matrix (4, 4, 0.0))
{
  position.set_id (POSITION);
  box.set_id (BOX);
  key.set_id (KEY);
  keybox.set_id (KEYBOX);
  keyreverse.set_id (KEYREVERSE);
  keypos.set_id (KEYPOS);
  colororder.set_id (COLORORDER);
  dataaspectratio.set_id (DATAASPECTRATIO);
  dataaspectratiomode.set_id (DATAASPECTRATIOMODE);
  layer.set_id (LAYER);
  xlim.set_id (XLIM);
  ylim.set_id (YLIM);
  zlim.set_id (ZLIM);
  clim.set_id (CLIM);
  alim.set_id (ALIM);
  xlimmode.set_id (XLIMMODE);
  ylimmode.set_id (YLIMMODE);
  zlimmode.set_id (ZLIMMODE);
  climmode.set_id (CLIMMODE);
  alimmode.set_id (ALIMMODE);
  xlabel.set_id (XLABEL);
  ylabel.set_id (YLABEL);
  zlabel.set_id (ZLABEL);
  title.set_id (TITLE);
  xgrid.set_id (XGRID);
  ygrid.set_id (YGRID);
  zgrid.set_id (ZGRID);
  xminorgrid.set_id (XMINORGRID);
  yminorgrid.set_id (YMINORGRID);
  zminorgrid.set_id (ZMINORGRID);
  xtick.set_id (XTICK);
  ytick.set_id (YTICK);
  ztick.set_id (ZTICK);
  xtickmode.set_id (XTICKMODE);
  ytickmode.set_id (YTICKMODE);
  ztickmode.set_id (ZTICKMODE);
  xminortick.set_id (XMINORTICK);
  yminortick.set_id (YMINORTICK);
  zminortick.set_id (ZMINORTICK);
  xticklabel.set_id (XTICKLABEL);
  yticklabel.set_id (YTICKLABEL);
  zticklabel.set_id (ZTICKLABEL);
  xticklabelmode.set_id (XTICKLABELMODE);
  yticklabelmode.set_id (YTICKLABELMODE);
  zticklabelmode.set_id (ZTICKLABELMODE);
  interpreter.set_id (INTERPRETER);
  color.set_id (COLOR);
  xcolor.set_id (XCOLOR);
  ycolor.set_id (YCOLOR);
  zcolor.set_id (ZCOLOR);
  xscale.set_id (XSCALE);
  yscale.set_id (YSCALE);
  zscale.set_id (ZSCALE);
  xdir.set_id (XDIR);
  ydir.set_id (YDIR);
  zdir.set_id (ZDIR);
  yaxislocation.set_id (YAXISLOCATION);
  xaxislocation.set_id (XAXISLOCATION);
  view.set_id (VIEW);
  nextplot.set_id (NEXTPLOT);
  outerposition.set_id (OUTERPOSITION);
  activepositionproperty.set_id (ACTIVEPOSITIONPROPERTY);
  ambientlightcolor.set_id (AMBIENTLIGHTCOLOR);
  cameraposition.set_id (CAMERAPOSITION);
  cameratarget.set_id (CAMERATARGET);
  cameraupvector.set_id (CAMERAUPVECTOR);
  cameraviewangle.set_id (CAMERAVIEWANGLE);
  camerapositionmode.set_id (CAMERAPOSITIONMODE);
  cameratargetmode.set_id (CAMERATARGETMODE);
  cameraupvectormode.set_id (CAMERAUPVECTORMODE);
  cameraviewanglemode.set_id (CAMERAVIEWANGLEMODE);
  currentpoint.set_id (CURRENTPOINT);
  drawmode.set_id (DRAWMODE);
  fontangle.set_id (FONTANGLE);
  fontname.set_id (FONTNAME);
  fontsize.set_id (FONTSIZE);
  fontunits.set_id (FONTUNITS);
  fontweight.set_id (FONTWEIGHT);
  gridlinestyle.set_id (GRIDLINESTYLE);
  linestyleorder.set_id (LINESTYLEORDER);
  linewidth.set_id (LINEWIDTH);
  minorgridlinestyle.set_id (MINORGRIDLINESTYLE);
  plotboxaspectratio.set_id (PLOTBOXASPECTRATIO);
  plotboxaspectratiomode.set_id (PLOTBOXASPECTRATIOMODE);
  projection.set_id (PROJECTION);
  tickdir.set_id (TICKDIR);
  tickdirmode.set_id (TICKDIRMODE);
  ticklength.set_id (TICKLENGTH);
  tightinset.set_id (TIGHTINSET);
  units.set_id (UNITS);
  x_viewtransform.set_id (X_VIEWTRANSFORM);
  x_viewtransform.set_hidden (true);
  x_projectiontransform.set_id (X_PROJECTIONTRANSFORM);
  x_projectiontransform.set_hidden (true);
  x_viewporttransform.set_id (X_VIEWPORTTRANSFORM);
  x_viewporttransform.set_hidden (true);
  x_normrendertransform.set_id (X_NORMRENDERTRANSFORM);
  x_normrendertransform.set_hidden (true);
  x_rendertransform.set_id (X_RENDERTRANSFORM);
  x_rendertransform.set_hidden (true);
  init ();
}

void
axes::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("position"))
    set_position (val);
  else if (pname.compare ("box"))
    set_box (val);
  else if (pname.compare ("key"))
    set_key (val);
  else if (pname.compare ("keybox"))
    set_keybox (val);
  else if (pname.compare ("keyreverse"))
    set_keyreverse (val);
  else if (pname.compare ("keypos"))
    set_keypos (val);
  else if (pname.compare ("colororder"))
    set_colororder (val);
  else if (pname.compare ("dataaspectratio"))
    set_dataaspectratio (val);
  else if (pname.compare ("dataaspectratiomode"))
    set_dataaspectratiomode (val);
  else if (pname.compare ("layer"))
    set_layer (val);
  else if (pname.compare ("xlim"))
    set_xlim (val);
  else if (pname.compare ("ylim"))
    set_ylim (val);
  else if (pname.compare ("zlim"))
    set_zlim (val);
  else if (pname.compare ("clim"))
    set_clim (val);
  else if (pname.compare ("alim"))
    set_alim (val);
  else if (pname.compare ("xlimmode"))
    set_xlimmode (val);
  else if (pname.compare ("ylimmode"))
    set_ylimmode (val);
  else if (pname.compare ("zlimmode"))
    set_zlimmode (val);
  else if (pname.compare ("climmode"))
    set_climmode (val);
  else if (pname.compare ("alimmode"))
    set_alimmode (val);
  else if (pname.compare ("xlabel"))
    set_xlabel (val);
  else if (pname.compare ("ylabel"))
    set_ylabel (val);
  else if (pname.compare ("zlabel"))
    set_zlabel (val);
  else if (pname.compare ("title"))
    set_title (val);
  else if (pname.compare ("xgrid"))
    set_xgrid (val);
  else if (pname.compare ("ygrid"))
    set_ygrid (val);
  else if (pname.compare ("zgrid"))
    set_zgrid (val);
  else if (pname.compare ("xminorgrid"))
    set_xminorgrid (val);
  else if (pname.compare ("yminorgrid"))
    set_yminorgrid (val);
  else if (pname.compare ("zminorgrid"))
    set_zminorgrid (val);
  else if (pname.compare ("xtick"))
    set_xtick (val);
  else if (pname.compare ("ytick"))
    set_ytick (val);
  else if (pname.compare ("ztick"))
    set_ztick (val);
  else if (pname.compare ("xtickmode"))
    set_xtickmode (val);
  else if (pname.compare ("ytickmode"))
    set_ytickmode (val);
  else if (pname.compare ("ztickmode"))
    set_ztickmode (val);
  else if (pname.compare ("xminortick"))
    set_xminortick (val);
  else if (pname.compare ("yminortick"))
    set_yminortick (val);
  else if (pname.compare ("zminortick"))
    set_zminortick (val);
  else if (pname.compare ("xticklabel"))
    set_xticklabel (val);
  else if (pname.compare ("yticklabel"))
    set_yticklabel (val);
  else if (pname.compare ("zticklabel"))
    set_zticklabel (val);
  else if (pname.compare ("xticklabelmode"))
    set_xticklabelmode (val);
  else if (pname.compare ("yticklabelmode"))
    set_yticklabelmode (val);
  else if (pname.compare ("zticklabelmode"))
    set_zticklabelmode (val);
  else if (pname.compare ("interpreter"))
    set_interpreter (val);
  else if (pname.compare ("color"))
    set_color (val);
  else if (pname.compare ("xcolor"))
    set_xcolor (val);
  else if (pname.compare ("ycolor"))
    set_ycolor (val);
  else if (pname.compare ("zcolor"))
    set_zcolor (val);
  else if (pname.compare ("xscale"))
    set_xscale (val);
  else if (pname.compare ("yscale"))
    set_yscale (val);
  else if (pname.compare ("zscale"))
    set_zscale (val);
  else if (pname.compare ("xdir"))
    set_xdir (val);
  else if (pname.compare ("ydir"))
    set_ydir (val);
  else if (pname.compare ("zdir"))
    set_zdir (val);
  else if (pname.compare ("yaxislocation"))
    set_yaxislocation (val);
  else if (pname.compare ("xaxislocation"))
    set_xaxislocation (val);
  else if (pname.compare ("view"))
    set_view (val);
  else if (pname.compare ("nextplot"))
    set_nextplot (val);
  else if (pname.compare ("outerposition"))
    set_outerposition (val);
  else if (pname.compare ("activepositionproperty"))
    set_activepositionproperty (val);
  else if (pname.compare ("ambientlightcolor"))
    set_ambientlightcolor (val);
  else if (pname.compare ("cameraposition"))
    set_cameraposition (val);
  else if (pname.compare ("cameratarget"))
    set_cameratarget (val);
  else if (pname.compare ("cameraupvector"))
    set_cameraupvector (val);
  else if (pname.compare ("cameraviewangle"))
    set_cameraviewangle (val);
  else if (pname.compare ("camerapositionmode"))
    set_camerapositionmode (val);
  else if (pname.compare ("cameratargetmode"))
    set_cameratargetmode (val);
  else if (pname.compare ("cameraupvectormode"))
    set_cameraupvectormode (val);
  else if (pname.compare ("cameraviewanglemode"))
    set_cameraviewanglemode (val);
  else if (pname.compare ("currentpoint"))
    set_currentpoint (val);
  else if (pname.compare ("drawmode"))
    set_drawmode (val);
  else if (pname.compare ("fontangle"))
    set_fontangle (val);
  else if (pname.compare ("fontname"))
    set_fontname (val);
  else if (pname.compare ("fontsize"))
    set_fontsize (val);
  else if (pname.compare ("fontunits"))
    set_fontunits (val);
  else if (pname.compare ("fontweight"))
    set_fontweight (val);
  else if (pname.compare ("gridlinestyle"))
    set_gridlinestyle (val);
  else if (pname.compare ("linestyleorder"))
    set_linestyleorder (val);
  else if (pname.compare ("linewidth"))
    set_linewidth (val);
  else if (pname.compare ("minorgridlinestyle"))
    set_minorgridlinestyle (val);
  else if (pname.compare ("plotboxaspectratio"))
    set_plotboxaspectratio (val);
  else if (pname.compare ("plotboxaspectratiomode"))
    set_plotboxaspectratiomode (val);
  else if (pname.compare ("projection"))
    set_projection (val);
  else if (pname.compare ("tickdir"))
    set_tickdir (val);
  else if (pname.compare ("tickdirmode"))
    set_tickdirmode (val);
  else if (pname.compare ("ticklength"))
    set_ticklength (val);
  else if (pname.compare ("units"))
    set_units (val);
  else if (pname.compare ("x_viewtransform"))
    set_x_viewtransform (val);
  else if (pname.compare ("x_projectiontransform"))
    set_x_projectiontransform (val);
  else if (pname.compare ("x_viewporttransform"))
    set_x_viewporttransform (val);
  else if (pname.compare ("x_normrendertransform"))
    set_x_normrendertransform (val);
  else if (pname.compare ("x_rendertransform"))
    set_x_rendertransform (val);
  else
    base_properties::set (pname, "axes", val);
}

octave_value
axes::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("position", get_position ());
  m.assign ("box", get_box ());
  m.assign ("key", get_key ());
  m.assign ("keybox", get_keybox ());
  m.assign ("keyreverse", get_keyreverse ());
  m.assign ("keypos", get_keypos ());
  m.assign ("colororder", get_colororder ());
  m.assign ("dataaspectratio", get_dataaspectratio ());
  m.assign ("dataaspectratiomode", get_dataaspectratiomode ());
  m.assign ("layer", get_layer ());
  m.assign ("xlim", get_xlim ());
  m.assign ("ylim", get_ylim ());
  m.assign ("zlim", get_zlim ());
  m.assign ("clim", get_clim ());
  m.assign ("alim", get_alim ());
  m.assign ("xlimmode", get_xlimmode ());
  m.assign ("ylimmode", get_ylimmode ());
  m.assign ("zlimmode", get_zlimmode ());
  m.assign ("climmode", get_climmode ());
  m.assign ("alimmode", get_alimmode ());
  m.assign ("xlabel", get_xlabel ().as_octave_value ());
  m.assign ("ylabel", get_ylabel ().as_octave_value ());
  m.assign ("zlabel", get_zlabel ().as_octave_value ());
  m.assign ("title", get_title ().as_octave_value ());
  m.assign ("xgrid", get_xgrid ());
  m.assign ("ygrid", get_ygrid ());
  m.assign ("zgrid", get_zgrid ());
  m.assign ("xminorgrid", get_xminorgrid ());
  m.assign ("yminorgrid", get_yminorgrid ());
  m.assign ("zminorgrid", get_zminorgrid ());
  m.assign ("xtick", get_xtick ());
  m.assign ("ytick", get_ytick ());
  m.assign ("ztick", get_ztick ());
  m.assign ("xtickmode", get_xtickmode ());
  m.assign ("ytickmode", get_ytickmode ());
  m.assign ("ztickmode", get_ztickmode ());
  m.assign ("xminortick", get_xminortick ());
  m.assign ("yminortick", get_yminortick ());
  m.assign ("zminortick", get_zminortick ());
  m.assign ("xticklabel", get_xticklabel ());
  m.assign ("yticklabel", get_yticklabel ());
  m.assign ("zticklabel", get_zticklabel ());
  m.assign ("xticklabelmode", get_xticklabelmode ());
  m.assign ("yticklabelmode", get_yticklabelmode ());
  m.assign ("zticklabelmode", get_zticklabelmode ());
  m.assign ("interpreter", get_interpreter ());
  m.assign ("color", get_color ());
  m.assign ("xcolor", get_xcolor ());
  m.assign ("ycolor", get_ycolor ());
  m.assign ("zcolor", get_zcolor ());
  m.assign ("xscale", get_xscale ());
  m.assign ("yscale", get_yscale ());
  m.assign ("zscale", get_zscale ());
  m.assign ("xdir", get_xdir ());
  m.assign ("ydir", get_ydir ());
  m.assign ("zdir", get_zdir ());
  m.assign ("yaxislocation", get_yaxislocation ());
  m.assign ("xaxislocation", get_xaxislocation ());
  m.assign ("view", get_view ());
  m.assign ("nextplot", get_nextplot ());
  m.assign ("outerposition", get_outerposition ());
  m.assign ("activepositionproperty", get_activepositionproperty ());
  m.assign ("ambientlightcolor", get_ambientlightcolor ());
  m.assign ("cameraposition", get_cameraposition ());
  m.assign ("cameratarget", get_cameratarget ());
  m.assign ("cameraupvector", get_cameraupvector ());
  m.assign ("cameraviewangle", get_cameraviewangle ());
  m.assign ("camerapositionmode", get_camerapositionmode ());
  m.assign ("cameratargetmode", get_cameratargetmode ());
  m.assign ("cameraupvectormode", get_cameraupvectormode ());
  m.assign ("cameraviewanglemode", get_cameraviewanglemode ());
  m.assign ("currentpoint", get_currentpoint ());
  m.assign ("drawmode", get_drawmode ());
  m.assign ("fontangle", get_fontangle ());
  m.assign ("fontname", get_fontname ());
  m.assign ("fontsize", get_fontsize ());
  m.assign ("fontunits", get_fontunits ());
  m.assign ("fontweight", get_fontweight ());
  m.assign ("gridlinestyle", get_gridlinestyle ());
  m.assign ("linestyleorder", get_linestyleorder ());
  m.assign ("linewidth", get_linewidth ());
  m.assign ("minorgridlinestyle", get_minorgridlinestyle ());
  m.assign ("plotboxaspectratio", get_plotboxaspectratio ());
  m.assign ("plotboxaspectratiomode", get_plotboxaspectratiomode ());
  m.assign ("projection", get_projection ());
  m.assign ("tickdir", get_tickdir ());
  m.assign ("tickdirmode", get_tickdirmode ());
  m.assign ("ticklength", get_ticklength ());
  m.assign ("tightinset", get_tightinset ());
  m.assign ("units", get_units ());
  if (all)
    m.assign ("x_viewtransform", get_x_viewtransform ());
  if (all)
    m.assign ("x_projectiontransform", get_x_projectiontransform ());
  if (all)
    m.assign ("x_viewporttransform", get_x_viewporttransform ());
  if (all)
    m.assign ("x_normrendertransform", get_x_normrendertransform ());
  if (all)
    m.assign ("x_rendertransform", get_x_rendertransform ());

  return m;
}

octave_value
axes::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("position"))
    retval = get_position ();
  else if (pname.compare ("box"))
    retval = get_box ();
  else if (pname.compare ("key"))
    retval = get_key ();
  else if (pname.compare ("keybox"))
    retval = get_keybox ();
  else if (pname.compare ("keyreverse"))
    retval = get_keyreverse ();
  else if (pname.compare ("keypos"))
    retval = get_keypos ();
  else if (pname.compare ("colororder"))
    retval = get_colororder ();
  else if (pname.compare ("dataaspectratio"))
    retval = get_dataaspectratio ();
  else if (pname.compare ("dataaspectratiomode"))
    retval = get_dataaspectratiomode ();
  else if (pname.compare ("layer"))
    retval = get_layer ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("clim"))
    retval = get_clim ();
  else if (pname.compare ("alim"))
    retval = get_alim ();
  else if (pname.compare ("xlimmode"))
    retval = get_xlimmode ();
  else if (pname.compare ("ylimmode"))
    retval = get_ylimmode ();
  else if (pname.compare ("zlimmode"))
    retval = get_zlimmode ();
  else if (pname.compare ("climmode"))
    retval = get_climmode ();
  else if (pname.compare ("alimmode"))
    retval = get_alimmode ();
  else if (pname.compare ("xlabel"))
    retval = get_xlabel ().as_octave_value ();
  else if (pname.compare ("ylabel"))
    retval = get_ylabel ().as_octave_value ();
  else if (pname.compare ("zlabel"))
    retval = get_zlabel ().as_octave_value ();
  else if (pname.compare ("title"))
    retval = get_title ().as_octave_value ();
  else if (pname.compare ("xgrid"))
    retval = get_xgrid ();
  else if (pname.compare ("ygrid"))
    retval = get_ygrid ();
  else if (pname.compare ("zgrid"))
    retval = get_zgrid ();
  else if (pname.compare ("xminorgrid"))
    retval = get_xminorgrid ();
  else if (pname.compare ("yminorgrid"))
    retval = get_yminorgrid ();
  else if (pname.compare ("zminorgrid"))
    retval = get_zminorgrid ();
  else if (pname.compare ("xtick"))
    retval = get_xtick ();
  else if (pname.compare ("ytick"))
    retval = get_ytick ();
  else if (pname.compare ("ztick"))
    retval = get_ztick ();
  else if (pname.compare ("xtickmode"))
    retval = get_xtickmode ();
  else if (pname.compare ("ytickmode"))
    retval = get_ytickmode ();
  else if (pname.compare ("ztickmode"))
    retval = get_ztickmode ();
  else if (pname.compare ("xminortick"))
    retval = get_xminortick ();
  else if (pname.compare ("yminortick"))
    retval = get_yminortick ();
  else if (pname.compare ("zminortick"))
    retval = get_zminortick ();
  else if (pname.compare ("xticklabel"))
    retval = get_xticklabel ();
  else if (pname.compare ("yticklabel"))
    retval = get_yticklabel ();
  else if (pname.compare ("zticklabel"))
    retval = get_zticklabel ();
  else if (pname.compare ("xticklabelmode"))
    retval = get_xticklabelmode ();
  else if (pname.compare ("yticklabelmode"))
    retval = get_yticklabelmode ();
  else if (pname.compare ("zticklabelmode"))
    retval = get_zticklabelmode ();
  else if (pname.compare ("interpreter"))
    retval = get_interpreter ();
  else if (pname.compare ("color"))
    retval = get_color ();
  else if (pname.compare ("xcolor"))
    retval = get_xcolor ();
  else if (pname.compare ("ycolor"))
    retval = get_ycolor ();
  else if (pname.compare ("zcolor"))
    retval = get_zcolor ();
  else if (pname.compare ("xscale"))
    retval = get_xscale ();
  else if (pname.compare ("yscale"))
    retval = get_yscale ();
  else if (pname.compare ("zscale"))
    retval = get_zscale ();
  else if (pname.compare ("xdir"))
    retval = get_xdir ();
  else if (pname.compare ("ydir"))
    retval = get_ydir ();
  else if (pname.compare ("zdir"))
    retval = get_zdir ();
  else if (pname.compare ("yaxislocation"))
    retval = get_yaxislocation ();
  else if (pname.compare ("xaxislocation"))
    retval = get_xaxislocation ();
  else if (pname.compare ("view"))
    retval = get_view ();
  else if (pname.compare ("nextplot"))
    retval = get_nextplot ();
  else if (pname.compare ("outerposition"))
    retval = get_outerposition ();
  else if (pname.compare ("activepositionproperty"))
    retval = get_activepositionproperty ();
  else if (pname.compare ("ambientlightcolor"))
    retval = get_ambientlightcolor ();
  else if (pname.compare ("cameraposition"))
    retval = get_cameraposition ();
  else if (pname.compare ("cameratarget"))
    retval = get_cameratarget ();
  else if (pname.compare ("cameraupvector"))
    retval = get_cameraupvector ();
  else if (pname.compare ("cameraviewangle"))
    retval = get_cameraviewangle ();
  else if (pname.compare ("camerapositionmode"))
    retval = get_camerapositionmode ();
  else if (pname.compare ("cameratargetmode"))
    retval = get_cameratargetmode ();
  else if (pname.compare ("cameraupvectormode"))
    retval = get_cameraupvectormode ();
  else if (pname.compare ("cameraviewanglemode"))
    retval = get_cameraviewanglemode ();
  else if (pname.compare ("currentpoint"))
    retval = get_currentpoint ();
  else if (pname.compare ("drawmode"))
    retval = get_drawmode ();
  else if (pname.compare ("fontangle"))
    retval = get_fontangle ();
  else if (pname.compare ("fontname"))
    retval = get_fontname ();
  else if (pname.compare ("fontsize"))
    retval = get_fontsize ();
  else if (pname.compare ("fontunits"))
    retval = get_fontunits ();
  else if (pname.compare ("fontweight"))
    retval = get_fontweight ();
  else if (pname.compare ("gridlinestyle"))
    retval = get_gridlinestyle ();
  else if (pname.compare ("linestyleorder"))
    retval = get_linestyleorder ();
  else if (pname.compare ("linewidth"))
    retval = get_linewidth ();
  else if (pname.compare ("minorgridlinestyle"))
    retval = get_minorgridlinestyle ();
  else if (pname.compare ("plotboxaspectratio"))
    retval = get_plotboxaspectratio ();
  else if (pname.compare ("plotboxaspectratiomode"))
    retval = get_plotboxaspectratiomode ();
  else if (pname.compare ("projection"))
    retval = get_projection ();
  else if (pname.compare ("tickdir"))
    retval = get_tickdir ();
  else if (pname.compare ("tickdirmode"))
    retval = get_tickdirmode ();
  else if (pname.compare ("ticklength"))
    retval = get_ticklength ();
  else if (pname.compare ("tightinset"))
    retval = get_tightinset ();
  else if (pname.compare ("units"))
    retval = get_units ();
  else if (pname.compare ("x_viewtransform"))
    retval = get_x_viewtransform ();
  else if (pname.compare ("x_projectiontransform"))
    retval = get_x_projectiontransform ();
  else if (pname.compare ("x_viewporttransform"))
    retval = get_x_viewporttransform ();
  else if (pname.compare ("x_normrendertransform"))
    retval = get_x_normrendertransform ();
  else if (pname.compare ("x_rendertransform"))
    retval = get_x_rendertransform ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
axes::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("position"))
    return property (&position, true);
  else if (pname.compare ("box"))
    return property (&box, true);
  else if (pname.compare ("key"))
    return property (&key, true);
  else if (pname.compare ("keybox"))
    return property (&keybox, true);
  else if (pname.compare ("keyreverse"))
    return property (&keyreverse, true);
  else if (pname.compare ("keypos"))
    return property (&keypos, true);
  else if (pname.compare ("colororder"))
    return property (&colororder, true);
  else if (pname.compare ("dataaspectratio"))
    return property (&dataaspectratio, true);
  else if (pname.compare ("dataaspectratiomode"))
    return property (&dataaspectratiomode, true);
  else if (pname.compare ("layer"))
    return property (&layer, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("clim"))
    return property (&clim, true);
  else if (pname.compare ("alim"))
    return property (&alim, true);
  else if (pname.compare ("xlimmode"))
    return property (&xlimmode, true);
  else if (pname.compare ("ylimmode"))
    return property (&ylimmode, true);
  else if (pname.compare ("zlimmode"))
    return property (&zlimmode, true);
  else if (pname.compare ("climmode"))
    return property (&climmode, true);
  else if (pname.compare ("alimmode"))
    return property (&alimmode, true);
  else if (pname.compare ("xlabel"))
    return property (&xlabel, true);
  else if (pname.compare ("ylabel"))
    return property (&ylabel, true);
  else if (pname.compare ("zlabel"))
    return property (&zlabel, true);
  else if (pname.compare ("title"))
    return property (&title, true);
  else if (pname.compare ("xgrid"))
    return property (&xgrid, true);
  else if (pname.compare ("ygrid"))
    return property (&ygrid, true);
  else if (pname.compare ("zgrid"))
    return property (&zgrid, true);
  else if (pname.compare ("xminorgrid"))
    return property (&xminorgrid, true);
  else if (pname.compare ("yminorgrid"))
    return property (&yminorgrid, true);
  else if (pname.compare ("zminorgrid"))
    return property (&zminorgrid, true);
  else if (pname.compare ("xtick"))
    return property (&xtick, true);
  else if (pname.compare ("ytick"))
    return property (&ytick, true);
  else if (pname.compare ("ztick"))
    return property (&ztick, true);
  else if (pname.compare ("xtickmode"))
    return property (&xtickmode, true);
  else if (pname.compare ("ytickmode"))
    return property (&ytickmode, true);
  else if (pname.compare ("ztickmode"))
    return property (&ztickmode, true);
  else if (pname.compare ("xminortick"))
    return property (&xminortick, true);
  else if (pname.compare ("yminortick"))
    return property (&yminortick, true);
  else if (pname.compare ("zminortick"))
    return property (&zminortick, true);
  else if (pname.compare ("xticklabel"))
    return property (&xticklabel, true);
  else if (pname.compare ("yticklabel"))
    return property (&yticklabel, true);
  else if (pname.compare ("zticklabel"))
    return property (&zticklabel, true);
  else if (pname.compare ("xticklabelmode"))
    return property (&xticklabelmode, true);
  else if (pname.compare ("yticklabelmode"))
    return property (&yticklabelmode, true);
  else if (pname.compare ("zticklabelmode"))
    return property (&zticklabelmode, true);
  else if (pname.compare ("interpreter"))
    return property (&interpreter, true);
  else if (pname.compare ("color"))
    return property (&color, true);
  else if (pname.compare ("xcolor"))
    return property (&xcolor, true);
  else if (pname.compare ("ycolor"))
    return property (&ycolor, true);
  else if (pname.compare ("zcolor"))
    return property (&zcolor, true);
  else if (pname.compare ("xscale"))
    return property (&xscale, true);
  else if (pname.compare ("yscale"))
    return property (&yscale, true);
  else if (pname.compare ("zscale"))
    return property (&zscale, true);
  else if (pname.compare ("xdir"))
    return property (&xdir, true);
  else if (pname.compare ("ydir"))
    return property (&ydir, true);
  else if (pname.compare ("zdir"))
    return property (&zdir, true);
  else if (pname.compare ("yaxislocation"))
    return property (&yaxislocation, true);
  else if (pname.compare ("xaxislocation"))
    return property (&xaxislocation, true);
  else if (pname.compare ("view"))
    return property (&view, true);
  else if (pname.compare ("nextplot"))
    return property (&nextplot, true);
  else if (pname.compare ("outerposition"))
    return property (&outerposition, true);
  else if (pname.compare ("activepositionproperty"))
    return property (&activepositionproperty, true);
  else if (pname.compare ("ambientlightcolor"))
    return property (&ambientlightcolor, true);
  else if (pname.compare ("cameraposition"))
    return property (&cameraposition, true);
  else if (pname.compare ("cameratarget"))
    return property (&cameratarget, true);
  else if (pname.compare ("cameraupvector"))
    return property (&cameraupvector, true);
  else if (pname.compare ("cameraviewangle"))
    return property (&cameraviewangle, true);
  else if (pname.compare ("camerapositionmode"))
    return property (&camerapositionmode, true);
  else if (pname.compare ("cameratargetmode"))
    return property (&cameratargetmode, true);
  else if (pname.compare ("cameraupvectormode"))
    return property (&cameraupvectormode, true);
  else if (pname.compare ("cameraviewanglemode"))
    return property (&cameraviewanglemode, true);
  else if (pname.compare ("currentpoint"))
    return property (&currentpoint, true);
  else if (pname.compare ("drawmode"))
    return property (&drawmode, true);
  else if (pname.compare ("fontangle"))
    return property (&fontangle, true);
  else if (pname.compare ("fontname"))
    return property (&fontname, true);
  else if (pname.compare ("fontsize"))
    return property (&fontsize, true);
  else if (pname.compare ("fontunits"))
    return property (&fontunits, true);
  else if (pname.compare ("fontweight"))
    return property (&fontweight, true);
  else if (pname.compare ("gridlinestyle"))
    return property (&gridlinestyle, true);
  else if (pname.compare ("linestyleorder"))
    return property (&linestyleorder, true);
  else if (pname.compare ("linewidth"))
    return property (&linewidth, true);
  else if (pname.compare ("minorgridlinestyle"))
    return property (&minorgridlinestyle, true);
  else if (pname.compare ("plotboxaspectratio"))
    return property (&plotboxaspectratio, true);
  else if (pname.compare ("plotboxaspectratiomode"))
    return property (&plotboxaspectratiomode, true);
  else if (pname.compare ("projection"))
    return property (&projection, true);
  else if (pname.compare ("tickdir"))
    return property (&tickdir, true);
  else if (pname.compare ("tickdirmode"))
    return property (&tickdirmode, true);
  else if (pname.compare ("ticklength"))
    return property (&ticklength, true);
  else if (pname.compare ("tightinset"))
    return property (&tightinset, true);
  else if (pname.compare ("units"))
    return property (&units, true);
  else if (pname.compare ("x_viewtransform"))
    return property (&x_viewtransform, true);
  else if (pname.compare ("x_projectiontransform"))
    return property (&x_projectiontransform, true);
  else if (pname.compare ("x_viewporttransform"))
    return property (&x_viewporttransform, true);
  else if (pname.compare ("x_normrendertransform"))
    return property (&x_normrendertransform, true);
  else if (pname.compare ("x_rendertransform"))
    return property (&x_rendertransform, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
axes::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["position"] = default_axes_position ();
  m["box"] = "on";
  m["key"] = "off";
  m["keybox"] = "off";
  m["keyreverse"] = "off";
  m["keypos"] = 1;
  m["colororder"] = default_colororder ();
  m["dataaspectratio"] = Matrix (1, 3, 1.0);
  m["dataaspectratiomode"] = "auto";
  m["layer"] = "bottom";
  m["xlim"] = default_lim ();
  m["ylim"] = default_lim ();
  m["zlim"] = default_lim ();
  m["clim"] = default_lim ();
  m["alim"] = default_lim ();
  m["xlimmode"] = "auto";
  m["ylimmode"] = "auto";
  m["zlimmode"] = "auto";
  m["climmode"] = "auto";
  m["alimmode"] = "auto";
  m["xgrid"] = "off";
  m["ygrid"] = "off";
  m["zgrid"] = "off";
  m["xminorgrid"] = "off";
  m["yminorgrid"] = "off";
  m["zminorgrid"] = "off";
  m["xtick"] = default_axes_tick ();
  m["ytick"] = default_axes_tick ();
  m["ztick"] = default_axes_tick ();
  m["xtickmode"] = "auto";
  m["ytickmode"] = "auto";
  m["ztickmode"] = "auto";
  m["xminortick"] = "off";
  m["yminortick"] = "off";
  m["zminortick"] = "off";
  m["xticklabel"] = "";
  m["yticklabel"] = "";
  m["zticklabel"] = "";
  m["xticklabelmode"] = "auto";
  m["yticklabelmode"] = "auto";
  m["zticklabelmode"] = "auto";
  m["interpreter"] = "none";
  m["color"] = octave_value ();
  m["xcolor"] = octave_value ();
  m["ycolor"] = octave_value ();
  m["zcolor"] = octave_value ();
  m["xscale"] = "linear";
  m["yscale"] = "linear";
  m["zscale"] = "linear";
  m["xdir"] = "normal";
  m["ydir"] = "normal";
  m["zdir"] = "normal";
  m["yaxislocation"] = "left";
  m["xaxislocation"] = "bottom";
  m["view"] = Matrix ();
  m["nextplot"] = "replace";
  m["outerposition"] = default_axes_outerposition ();
  m["activepositionproperty"] = "outerposition";
  m["ambientlightcolor"] = octave_value ();
  m["cameraposition"] = Matrix (1, 3, 0.0);
  m["cameratarget"] = Matrix (1, 3, 0.0);
  m["cameraupvector"] = Matrix ();
  m["cameraviewangle"] = 10.0;
  m["camerapositionmode"] = "auto";
  m["cameratargetmode"] = "auto";
  m["cameraupvectormode"] = "auto";
  m["cameraviewanglemode"] = "auto";
  m["currentpoint"] = Matrix (2, 3, 0.0);
  m["drawmode"] = "normal";
  m["fontangle"] = "normal";
  m["fontname"] = OCTAVE_DEFAULT_FONTNAME;
  m["fontsize"] = 12;
  m["fontunits"] = "points";
  m["fontweight"] = "normal";
  m["gridlinestyle"] = ":";
  m["linestyleorder"] = "-";
  m["linewidth"] = 0.5;
  m["minorgridlinestyle"] = ":";
  m["plotboxaspectratio"] = Matrix (1, 3, 1.0);
  m["plotboxaspectratiomode"] = "auto";
  m["projection"] = "orthographic";
  m["tickdir"] = "in";
  m["tickdirmode"] = "auto";
  m["ticklength"] = default_axes_ticklength ();
  m["tightinset"] = Matrix (1, 4, 0.0);
  m["units"] = "normalized";
  m["x_viewtransform"] = Matrix (4, 4, 0.0);
  m["x_projectiontransform"] = Matrix (4, 4, 0.0);
  m["x_viewporttransform"] = Matrix (4, 4, 0.0);
  m["x_normrendertransform"] = Matrix (4, 4, 0.0);
  m["x_rendertransform"] = Matrix (4, 4, 0.0);

  return m;
}

std::string axes::properties::go_name ("axes");

bool axes::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("position");
      all_properties.insert ("box");
      all_properties.insert ("key");
      all_properties.insert ("keybox");
      all_properties.insert ("keyreverse");
      all_properties.insert ("keypos");
      all_properties.insert ("colororder");
      all_properties.insert ("dataaspectratio");
      all_properties.insert ("dataaspectratiomode");
      all_properties.insert ("layer");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("clim");
      all_properties.insert ("alim");
      all_properties.insert ("xlimmode");
      all_properties.insert ("ylimmode");
      all_properties.insert ("zlimmode");
      all_properties.insert ("climmode");
      all_properties.insert ("alimmode");
      all_properties.insert ("xlabel");
      all_properties.insert ("ylabel");
      all_properties.insert ("zlabel");
      all_properties.insert ("title");
      all_properties.insert ("xgrid");
      all_properties.insert ("ygrid");
      all_properties.insert ("zgrid");
      all_properties.insert ("xminorgrid");
      all_properties.insert ("yminorgrid");
      all_properties.insert ("zminorgrid");
      all_properties.insert ("xtick");
      all_properties.insert ("ytick");
      all_properties.insert ("ztick");
      all_properties.insert ("xtickmode");
      all_properties.insert ("ytickmode");
      all_properties.insert ("ztickmode");
      all_properties.insert ("xminortick");
      all_properties.insert ("yminortick");
      all_properties.insert ("zminortick");
      all_properties.insert ("xticklabel");
      all_properties.insert ("yticklabel");
      all_properties.insert ("zticklabel");
      all_properties.insert ("xticklabelmode");
      all_properties.insert ("yticklabelmode");
      all_properties.insert ("zticklabelmode");
      all_properties.insert ("interpreter");
      all_properties.insert ("color");
      all_properties.insert ("xcolor");
      all_properties.insert ("ycolor");
      all_properties.insert ("zcolor");
      all_properties.insert ("xscale");
      all_properties.insert ("yscale");
      all_properties.insert ("zscale");
      all_properties.insert ("xdir");
      all_properties.insert ("ydir");
      all_properties.insert ("zdir");
      all_properties.insert ("yaxislocation");
      all_properties.insert ("xaxislocation");
      all_properties.insert ("view");
      all_properties.insert ("nextplot");
      all_properties.insert ("outerposition");
      all_properties.insert ("activepositionproperty");
      all_properties.insert ("ambientlightcolor");
      all_properties.insert ("cameraposition");
      all_properties.insert ("cameratarget");
      all_properties.insert ("cameraupvector");
      all_properties.insert ("cameraviewangle");
      all_properties.insert ("camerapositionmode");
      all_properties.insert ("cameratargetmode");
      all_properties.insert ("cameraupvectormode");
      all_properties.insert ("cameraviewanglemode");
      all_properties.insert ("currentpoint");
      all_properties.insert ("drawmode");
      all_properties.insert ("fontangle");
      all_properties.insert ("fontname");
      all_properties.insert ("fontsize");
      all_properties.insert ("fontunits");
      all_properties.insert ("fontweight");
      all_properties.insert ("gridlinestyle");
      all_properties.insert ("linestyleorder");
      all_properties.insert ("linewidth");
      all_properties.insert ("minorgridlinestyle");
      all_properties.insert ("plotboxaspectratio");
      all_properties.insert ("plotboxaspectratiomode");
      all_properties.insert ("projection");
      all_properties.insert ("tickdir");
      all_properties.insert ("tickdirmode");
      all_properties.insert ("ticklength");
      all_properties.insert ("tightinset");
      all_properties.insert ("units");
      all_properties.insert ("x_viewtransform");
      all_properties.insert ("x_projectiontransform");
      all_properties.insert ("x_viewporttransform");
      all_properties.insert ("x_normrendertransform");
      all_properties.insert ("x_rendertransform");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "axes");
}

// ******** line ********

line::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    xdata ("xdata", mh, default_data ()),
    ydata ("ydata", mh, default_data ()),
    zdata ("zdata", mh, Matrix ()),
    ldata ("ldata", mh, Matrix ()),
    udata ("udata", mh, Matrix ()),
    xldata ("xldata", mh, Matrix ()),
    xudata ("xudata", mh, Matrix ()),
    xdatasource ("xdatasource", mh, ""),
    ydatasource ("ydatasource", mh, ""),
    zdatasource ("zdatasource", mh, ""),
    color ("color", mh, color_values (0, 0, 0)),
    linestyle ("linestyle", mh, "{-}|--|:|-.|none"),
    linewidth ("linewidth", mh, 0.5),
    marker ("marker", mh, "{none}|s|o|x|+|.|*|<|>|v|^|d|p|h"),
    markeredgecolor ("markeredgecolor", mh, "{auto}|none"),
    markerfacecolor ("markerfacecolor", mh, "auto|{none}"),
    markersize ("markersize", mh, 6),
    keylabel ("keylabel", mh, ""),
    interpreter ("interpreter", mh, "{tex}|none|latex"),
    displayname ("displayname", mh, ""),
    erasemode ("erasemode", mh, "{normal}|none|xor|background"),
    xlim ("xlim", mh, Matrix ()),
    ylim ("ylim", mh, Matrix ()),
    zlim ("zlim", mh, Matrix ()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    zliminclude ("zliminclude", mh, "off")
{
  xdata.set_id (XDATA);
  ydata.set_id (YDATA);
  zdata.set_id (ZDATA);
  ldata.set_id (LDATA);
  udata.set_id (UDATA);
  xldata.set_id (XLDATA);
  xudata.set_id (XUDATA);
  xdatasource.set_id (XDATASOURCE);
  ydatasource.set_id (YDATASOURCE);
  zdatasource.set_id (ZDATASOURCE);
  color.set_id (COLOR);
  linestyle.set_id (LINESTYLE);
  linewidth.set_id (LINEWIDTH);
  marker.set_id (MARKER);
  markeredgecolor.set_id (MARKEREDGECOLOR);
  markerfacecolor.set_id (MARKERFACECOLOR);
  markersize.set_id (MARKERSIZE);
  keylabel.set_id (KEYLABEL);
  interpreter.set_id (INTERPRETER);
  displayname.set_id (DISPLAYNAME);
  erasemode.set_id (ERASEMODE);
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  zlim.set_id (ZLIM);
  zlim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  zliminclude.set_id (ZLIMINCLUDE);
  zliminclude.set_hidden (true);
  init ();
}

void
line::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("xdata"))
    set_xdata (val);
  else if (pname.compare ("ydata"))
    set_ydata (val);
  else if (pname.compare ("zdata"))
    set_zdata (val);
  else if (pname.compare ("ldata"))
    set_ldata (val);
  else if (pname.compare ("udata"))
    set_udata (val);
  else if (pname.compare ("xldata"))
    set_xldata (val);
  else if (pname.compare ("xudata"))
    set_xudata (val);
  else if (pname.compare ("xdatasource"))
    set_xdatasource (val);
  else if (pname.compare ("ydatasource"))
    set_ydatasource (val);
  else if (pname.compare ("zdatasource"))
    set_zdatasource (val);
  else if (pname.compare ("color"))
    set_color (val);
  else if (pname.compare ("linestyle"))
    set_linestyle (val);
  else if (pname.compare ("linewidth"))
    set_linewidth (val);
  else if (pname.compare ("marker"))
    set_marker (val);
  else if (pname.compare ("markeredgecolor"))
    set_markeredgecolor (val);
  else if (pname.compare ("markerfacecolor"))
    set_markerfacecolor (val);
  else if (pname.compare ("markersize"))
    set_markersize (val);
  else if (pname.compare ("keylabel"))
    set_keylabel (val);
  else if (pname.compare ("interpreter"))
    set_interpreter (val);
  else if (pname.compare ("displayname"))
    set_displayname (val);
  else if (pname.compare ("erasemode"))
    set_erasemode (val);
  else if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("zliminclude"))
    set_zliminclude (val);
  else
    base_properties::set (pname, "line", val);
}

octave_value
line::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("xdata", get_xdata ());
  m.assign ("ydata", get_ydata ());
  m.assign ("zdata", get_zdata ());
  m.assign ("ldata", get_ldata ());
  m.assign ("udata", get_udata ());
  m.assign ("xldata", get_xldata ());
  m.assign ("xudata", get_xudata ());
  m.assign ("xdatasource", get_xdatasource ());
  m.assign ("ydatasource", get_ydatasource ());
  m.assign ("zdatasource", get_zdatasource ());
  m.assign ("color", get_color ());
  m.assign ("linestyle", get_linestyle ());
  m.assign ("linewidth", get_linewidth ());
  m.assign ("marker", get_marker ());
  m.assign ("markeredgecolor", get_markeredgecolor ());
  m.assign ("markerfacecolor", get_markerfacecolor ());
  m.assign ("markersize", get_markersize ());
  m.assign ("keylabel", get_keylabel ());
  m.assign ("interpreter", get_interpreter ());
  m.assign ("displayname", get_displayname ());
  m.assign ("erasemode", get_erasemode ());
  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("zlim", get_zlim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("zliminclude", get_zliminclude ());

  return m;
}

octave_value
line::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("xdata"))
    retval = get_xdata ();
  else if (pname.compare ("ydata"))
    retval = get_ydata ();
  else if (pname.compare ("zdata"))
    retval = get_zdata ();
  else if (pname.compare ("ldata"))
    retval = get_ldata ();
  else if (pname.compare ("udata"))
    retval = get_udata ();
  else if (pname.compare ("xldata"))
    retval = get_xldata ();
  else if (pname.compare ("xudata"))
    retval = get_xudata ();
  else if (pname.compare ("xdatasource"))
    retval = get_xdatasource ();
  else if (pname.compare ("ydatasource"))
    retval = get_ydatasource ();
  else if (pname.compare ("zdatasource"))
    retval = get_zdatasource ();
  else if (pname.compare ("color"))
    retval = get_color ();
  else if (pname.compare ("linestyle"))
    retval = get_linestyle ();
  else if (pname.compare ("linewidth"))
    retval = get_linewidth ();
  else if (pname.compare ("marker"))
    retval = get_marker ();
  else if (pname.compare ("markeredgecolor"))
    retval = get_markeredgecolor ();
  else if (pname.compare ("markerfacecolor"))
    retval = get_markerfacecolor ();
  else if (pname.compare ("markersize"))
    retval = get_markersize ();
  else if (pname.compare ("keylabel"))
    retval = get_keylabel ();
  else if (pname.compare ("interpreter"))
    retval = get_interpreter ();
  else if (pname.compare ("displayname"))
    retval = get_displayname ();
  else if (pname.compare ("erasemode"))
    retval = get_erasemode ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("zliminclude"))
    retval = get_zliminclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
line::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("xdata"))
    return property (&xdata, true);
  else if (pname.compare ("ydata"))
    return property (&ydata, true);
  else if (pname.compare ("zdata"))
    return property (&zdata, true);
  else if (pname.compare ("ldata"))
    return property (&ldata, true);
  else if (pname.compare ("udata"))
    return property (&udata, true);
  else if (pname.compare ("xldata"))
    return property (&xldata, true);
  else if (pname.compare ("xudata"))
    return property (&xudata, true);
  else if (pname.compare ("xdatasource"))
    return property (&xdatasource, true);
  else if (pname.compare ("ydatasource"))
    return property (&ydatasource, true);
  else if (pname.compare ("zdatasource"))
    return property (&zdatasource, true);
  else if (pname.compare ("color"))
    return property (&color, true);
  else if (pname.compare ("linestyle"))
    return property (&linestyle, true);
  else if (pname.compare ("linewidth"))
    return property (&linewidth, true);
  else if (pname.compare ("marker"))
    return property (&marker, true);
  else if (pname.compare ("markeredgecolor"))
    return property (&markeredgecolor, true);
  else if (pname.compare ("markerfacecolor"))
    return property (&markerfacecolor, true);
  else if (pname.compare ("markersize"))
    return property (&markersize, true);
  else if (pname.compare ("keylabel"))
    return property (&keylabel, true);
  else if (pname.compare ("interpreter"))
    return property (&interpreter, true);
  else if (pname.compare ("displayname"))
    return property (&displayname, true);
  else if (pname.compare ("erasemode"))
    return property (&erasemode, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("zliminclude"))
    return property (&zliminclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
line::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["xdata"] = default_data ();
  m["ydata"] = default_data ();
  m["zdata"] = Matrix ();
  m["ldata"] = Matrix ();
  m["udata"] = Matrix ();
  m["xldata"] = Matrix ();
  m["xudata"] = Matrix ();
  m["xdatasource"] = "";
  m["ydatasource"] = "";
  m["zdatasource"] = "";
  m["color"] = octave_value ();
  m["linestyle"] = "-";
  m["linewidth"] = 0.5;
  m["marker"] = "none";
  m["markeredgecolor"] = "auto";
  m["markerfacecolor"] = "none";
  m["markersize"] = 6;
  m["keylabel"] = "";
  m["interpreter"] = "tex";
  m["displayname"] = "";
  m["erasemode"] = "normal";
  m["xlim"] = Matrix ();
  m["ylim"] = Matrix ();
  m["zlim"] = Matrix ();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["zliminclude"] = "off";

  return m;
}

std::string line::properties::go_name ("line");

bool line::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("xdata");
      all_properties.insert ("ydata");
      all_properties.insert ("zdata");
      all_properties.insert ("ldata");
      all_properties.insert ("udata");
      all_properties.insert ("xldata");
      all_properties.insert ("xudata");
      all_properties.insert ("xdatasource");
      all_properties.insert ("ydatasource");
      all_properties.insert ("zdatasource");
      all_properties.insert ("color");
      all_properties.insert ("linestyle");
      all_properties.insert ("linewidth");
      all_properties.insert ("marker");
      all_properties.insert ("markeredgecolor");
      all_properties.insert ("markerfacecolor");
      all_properties.insert ("markersize");
      all_properties.insert ("keylabel");
      all_properties.insert ("interpreter");
      all_properties.insert ("displayname");
      all_properties.insert ("erasemode");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("zliminclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "line");
}

// ******** text ********

text::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    string ("string", mh, ""),
    units ("units", mh, "{data}|pixels|normalized|inches|centimeters|points"),
    position ("position", mh, Matrix (1, 3, 0.0)),
    rotation ("rotation", mh, 0),
    horizontalalignment ("horizontalalignment", mh, "{left}|center|right"),
    color ("color", mh, color_values (0, 0, 0)),
    fontname ("fontname", mh, OCTAVE_DEFAULT_FONTNAME),
    fontsize ("fontsize", mh, 10),
    fontangle ("fontangle", mh, "{normal}|italic|oblique"),
    fontweight ("fontweight", mh, "light|{normal}|demi|bold"),
    interpreter ("interpreter", mh, "{tex}|none|latex"),
    backgroundcolor ("backgroundcolor", mh, "{none}"),
    displayname ("displayname", mh, ""),
    edgecolor ("edgecolor", mh, "{none}"),
    erasemode ("erasemode", mh, "{normal}|none|xor|background"),
    editing ("editing", mh, "off"),
    fontunits ("fontunits", mh, "inches|centimeters|normalized|{points}|pixels"),
    linestyle ("linestyle", mh, "{-}|--|:|-.|none"),
    linewidth ("linewidth", mh, 0.5),
    margin ("margin", mh, 1),
    verticalalignment ("verticalalignment", mh, "top|cap|{middle}|baseline|bottom"),
    xlim ("xlim", mh, Matrix ()),
    ylim ("ylim", mh, Matrix ()),
    zlim ("zlim", mh, Matrix ()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    zliminclude ("zliminclude", mh, "off")
{
  string.set_id (STRING);
  units.set_id (UNITS);
  position.set_id (POSITION);
  rotation.set_id (ROTATION);
  horizontalalignment.set_id (HORIZONTALALIGNMENT);
  color.set_id (COLOR);
  fontname.set_id (FONTNAME);
  fontsize.set_id (FONTSIZE);
  fontangle.set_id (FONTANGLE);
  fontweight.set_id (FONTWEIGHT);
  interpreter.set_id (INTERPRETER);
  backgroundcolor.set_id (BACKGROUNDCOLOR);
  displayname.set_id (DISPLAYNAME);
  edgecolor.set_id (EDGECOLOR);
  erasemode.set_id (ERASEMODE);
  editing.set_id (EDITING);
  fontunits.set_id (FONTUNITS);
  linestyle.set_id (LINESTYLE);
  linewidth.set_id (LINEWIDTH);
  margin.set_id (MARGIN);
  verticalalignment.set_id (VERTICALALIGNMENT);
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  zlim.set_id (ZLIM);
  zlim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  zliminclude.set_id (ZLIMINCLUDE);
  zliminclude.set_hidden (true);
  init ();
}

void
text::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("string"))
    set_string (val);
  else if (pname.compare ("units"))
    set_units (val);
  else if (pname.compare ("position"))
    set_position (val);
  else if (pname.compare ("rotation"))
    set_rotation (val);
  else if (pname.compare ("horizontalalignment"))
    set_horizontalalignment (val);
  else if (pname.compare ("color"))
    set_color (val);
  else if (pname.compare ("fontname"))
    set_fontname (val);
  else if (pname.compare ("fontsize"))
    set_fontsize (val);
  else if (pname.compare ("fontangle"))
    set_fontangle (val);
  else if (pname.compare ("fontweight"))
    set_fontweight (val);
  else if (pname.compare ("interpreter"))
    set_interpreter (val);
  else if (pname.compare ("backgroundcolor"))
    set_backgroundcolor (val);
  else if (pname.compare ("displayname"))
    set_displayname (val);
  else if (pname.compare ("edgecolor"))
    set_edgecolor (val);
  else if (pname.compare ("erasemode"))
    set_erasemode (val);
  else if (pname.compare ("editing"))
    set_editing (val);
  else if (pname.compare ("fontunits"))
    set_fontunits (val);
  else if (pname.compare ("linestyle"))
    set_linestyle (val);
  else if (pname.compare ("linewidth"))
    set_linewidth (val);
  else if (pname.compare ("margin"))
    set_margin (val);
  else if (pname.compare ("verticalalignment"))
    set_verticalalignment (val);
  else if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("zliminclude"))
    set_zliminclude (val);
  else
    base_properties::set (pname, "text", val);
}

octave_value
text::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("string", get_string ());
  m.assign ("units", get_units ());
  m.assign ("position", get_position ());
  m.assign ("rotation", get_rotation ());
  m.assign ("horizontalalignment", get_horizontalalignment ());
  m.assign ("color", get_color ());
  m.assign ("fontname", get_fontname ());
  m.assign ("fontsize", get_fontsize ());
  m.assign ("fontangle", get_fontangle ());
  m.assign ("fontweight", get_fontweight ());
  m.assign ("interpreter", get_interpreter ());
  m.assign ("backgroundcolor", get_backgroundcolor ());
  m.assign ("displayname", get_displayname ());
  m.assign ("edgecolor", get_edgecolor ());
  m.assign ("erasemode", get_erasemode ());
  m.assign ("editing", get_editing ());
  m.assign ("fontunits", get_fontunits ());
  m.assign ("linestyle", get_linestyle ());
  m.assign ("linewidth", get_linewidth ());
  m.assign ("margin", get_margin ());
  m.assign ("verticalalignment", get_verticalalignment ());
  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("zlim", get_zlim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("zliminclude", get_zliminclude ());

  return m;
}

octave_value
text::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("string"))
    retval = get_string ();
  else if (pname.compare ("units"))
    retval = get_units ();
  else if (pname.compare ("position"))
    retval = get_position ();
  else if (pname.compare ("rotation"))
    retval = get_rotation ();
  else if (pname.compare ("horizontalalignment"))
    retval = get_horizontalalignment ();
  else if (pname.compare ("color"))
    retval = get_color ();
  else if (pname.compare ("fontname"))
    retval = get_fontname ();
  else if (pname.compare ("fontsize"))
    retval = get_fontsize ();
  else if (pname.compare ("fontangle"))
    retval = get_fontangle ();
  else if (pname.compare ("fontweight"))
    retval = get_fontweight ();
  else if (pname.compare ("interpreter"))
    retval = get_interpreter ();
  else if (pname.compare ("backgroundcolor"))
    retval = get_backgroundcolor ();
  else if (pname.compare ("displayname"))
    retval = get_displayname ();
  else if (pname.compare ("edgecolor"))
    retval = get_edgecolor ();
  else if (pname.compare ("erasemode"))
    retval = get_erasemode ();
  else if (pname.compare ("editing"))
    retval = get_editing ();
  else if (pname.compare ("fontunits"))
    retval = get_fontunits ();
  else if (pname.compare ("linestyle"))
    retval = get_linestyle ();
  else if (pname.compare ("linewidth"))
    retval = get_linewidth ();
  else if (pname.compare ("margin"))
    retval = get_margin ();
  else if (pname.compare ("verticalalignment"))
    retval = get_verticalalignment ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("zliminclude"))
    retval = get_zliminclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
text::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("string"))
    return property (&string, true);
  else if (pname.compare ("units"))
    return property (&units, true);
  else if (pname.compare ("position"))
    return property (&position, true);
  else if (pname.compare ("rotation"))
    return property (&rotation, true);
  else if (pname.compare ("horizontalalignment"))
    return property (&horizontalalignment, true);
  else if (pname.compare ("color"))
    return property (&color, true);
  else if (pname.compare ("fontname"))
    return property (&fontname, true);
  else if (pname.compare ("fontsize"))
    return property (&fontsize, true);
  else if (pname.compare ("fontangle"))
    return property (&fontangle, true);
  else if (pname.compare ("fontweight"))
    return property (&fontweight, true);
  else if (pname.compare ("interpreter"))
    return property (&interpreter, true);
  else if (pname.compare ("backgroundcolor"))
    return property (&backgroundcolor, true);
  else if (pname.compare ("displayname"))
    return property (&displayname, true);
  else if (pname.compare ("edgecolor"))
    return property (&edgecolor, true);
  else if (pname.compare ("erasemode"))
    return property (&erasemode, true);
  else if (pname.compare ("editing"))
    return property (&editing, true);
  else if (pname.compare ("fontunits"))
    return property (&fontunits, true);
  else if (pname.compare ("linestyle"))
    return property (&linestyle, true);
  else if (pname.compare ("linewidth"))
    return property (&linewidth, true);
  else if (pname.compare ("margin"))
    return property (&margin, true);
  else if (pname.compare ("verticalalignment"))
    return property (&verticalalignment, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("zliminclude"))
    return property (&zliminclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
text::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["string"] = "";
  m["units"] = "data";
  m["position"] = Matrix (1, 3, 0.0);
  m["rotation"] = 0;
  m["horizontalalignment"] = "left";
  m["color"] = octave_value ();
  m["fontname"] = OCTAVE_DEFAULT_FONTNAME;
  m["fontsize"] = 10;
  m["fontangle"] = "normal";
  m["fontweight"] = "normal";
  m["interpreter"] = "tex";
  m["backgroundcolor"] = "none";
  m["displayname"] = "";
  m["edgecolor"] = "none";
  m["erasemode"] = "normal";
  m["editing"] = "off";
  m["fontunits"] = "points";
  m["linestyle"] = "-";
  m["linewidth"] = 0.5;
  m["margin"] = 1;
  m["verticalalignment"] = "middle";
  m["xlim"] = Matrix ();
  m["ylim"] = Matrix ();
  m["zlim"] = Matrix ();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["zliminclude"] = "off";

  return m;
}

std::string text::properties::go_name ("text");

bool text::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("string");
      all_properties.insert ("units");
      all_properties.insert ("position");
      all_properties.insert ("rotation");
      all_properties.insert ("horizontalalignment");
      all_properties.insert ("color");
      all_properties.insert ("fontname");
      all_properties.insert ("fontsize");
      all_properties.insert ("fontangle");
      all_properties.insert ("fontweight");
      all_properties.insert ("interpreter");
      all_properties.insert ("backgroundcolor");
      all_properties.insert ("displayname");
      all_properties.insert ("edgecolor");
      all_properties.insert ("erasemode");
      all_properties.insert ("editing");
      all_properties.insert ("fontunits");
      all_properties.insert ("linestyle");
      all_properties.insert ("linewidth");
      all_properties.insert ("margin");
      all_properties.insert ("verticalalignment");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("zliminclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "text");
}

// ******** image ********

image::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    xdata ("xdata", mh, Matrix ()),
    ydata ("ydata", mh, Matrix ()),
    cdata ("cdata", mh, Matrix ()),
    cdatamapping ("cdatamapping", mh, "{scaled}|direct"),
    xlim ("xlim", mh, Matrix()),
    ylim ("ylim", mh, Matrix()),
    clim ("clim", mh, Matrix()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    climinclude ("climinclude", mh, "on")
{
  xdata.set_id (XDATA);
  ydata.set_id (YDATA);
  cdata.set_id (CDATA);
  cdatamapping.set_id (CDATAMAPPING);
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  clim.set_id (CLIM);
  clim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  climinclude.set_id (CLIMINCLUDE);
  climinclude.set_hidden (true);
  init ();
}

void
image::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("xdata"))
    set_xdata (val);
  else if (pname.compare ("ydata"))
    set_ydata (val);
  else if (pname.compare ("cdata"))
    set_cdata (val);
  else if (pname.compare ("cdatamapping"))
    set_cdatamapping (val);
  else if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("climinclude"))
    set_climinclude (val);
  else
    base_properties::set (pname, "image", val);
}

octave_value
image::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("xdata", get_xdata ());
  m.assign ("ydata", get_ydata ());
  m.assign ("cdata", get_cdata ());
  m.assign ("cdatamapping", get_cdatamapping ());
  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("clim", get_clim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("climinclude", get_climinclude ());

  return m;
}

octave_value
image::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("xdata"))
    retval = get_xdata ();
  else if (pname.compare ("ydata"))
    retval = get_ydata ();
  else if (pname.compare ("cdata"))
    retval = get_cdata ();
  else if (pname.compare ("cdatamapping"))
    retval = get_cdatamapping ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("clim"))
    retval = get_clim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("climinclude"))
    retval = get_climinclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
image::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("xdata"))
    return property (&xdata, true);
  else if (pname.compare ("ydata"))
    return property (&ydata, true);
  else if (pname.compare ("cdata"))
    return property (&cdata, true);
  else if (pname.compare ("cdatamapping"))
    return property (&cdatamapping, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("clim"))
    return property (&clim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("climinclude"))
    return property (&climinclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
image::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["xdata"] = Matrix ();
  m["ydata"] = Matrix ();
  m["cdata"] = Matrix ();
  m["cdatamapping"] = "scaled";
  m["xlim"] = Matrix();
  m["ylim"] = Matrix();
  m["clim"] = Matrix();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["climinclude"] = "on";

  return m;
}

std::string image::properties::go_name ("image");

bool image::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("xdata");
      all_properties.insert ("ydata");
      all_properties.insert ("cdata");
      all_properties.insert ("cdatamapping");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("clim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("climinclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "image");
}

// ******** patch ********

patch::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    xdata ("xdata", mh, Matrix ()),
    ydata ("ydata", mh, Matrix ()),
    zdata ("zdata", mh, Matrix ()),
    cdata ("cdata", mh, Matrix ()),
    cdatamapping ("cdatamapping", mh, "{scaled}|direct"),
    faces ("faces", mh, Matrix ()),
    facevertexalphadata ("facevertexalphadata", mh, Matrix ()),
    facevertexcdata ("facevertexcdata", mh, Matrix ()),
    vertices ("vertices", mh, Matrix ()),
    vertexnormals ("vertexnormals", mh, Matrix ()),
    normalmode ("normalmode", mh, "{auto}|manual"),
    facecolor ("facecolor", mh, "{flat}|none|interp"),
    facealpha ("facealpha", mh, double_radio_property (1.0, radio_values ("flat|interp"))),
    facelighting ("facelighting", mh, "flat|{none}|gouraud|phong"),
    edgecolor ("edgecolor", mh, color_property (color_values (0, 0, 0), radio_values ("flat|none|interp"))),
    edgealpha ("edgealpha", mh, double_radio_property (1.0, radio_values ("flat|interp"))),
    edgelighting ("edgelighting", mh, "{none}|flat|gouraud|phong"),
    backfacelighting ("backfacelighting", mh, "{reverselit}|unlit|lit"),
    ambientstrength ("ambientstrength", mh, 0.3),
    diffusestrength ("diffusestrength", mh, 0.6),
    specularstrength ("specularstrength", mh, 0.6),
    specularexponent ("specularexponent", mh, 10.0),
    specularcolorreflectance ("specularcolorreflectance", mh, 1.0),
    erasemode ("erasemode", mh, "{normal}|background|xor|none"),
    linestyle ("linestyle", mh, "{-}|--|:|-.|none"),
    linewidth ("linewidth", mh, 0.5),
    marker ("marker", mh, "{none}|s|o|x|+|.|*|<|>|v|^|d|p|h"),
    markeredgecolor ("markeredgecolor", mh, "{auto}|none"),
    markerfacecolor ("markerfacecolor", mh, "auto|{none}"),
    markersize ("markersize", mh, 6),
    keylabel ("keylabel", mh, ""),
    interpreter ("interpreter", mh, "{tex}|none|latex"),
    alphadatamapping ("alphadatamapping", mh, "none|{scaled}|direct"),
    xlim ("xlim", mh, Matrix ()),
    ylim ("ylim", mh, Matrix ()),
    zlim ("zlim", mh, Matrix ()),
    clim ("clim", mh, Matrix ()),
    alim ("alim", mh, Matrix ()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    zliminclude ("zliminclude", mh, "on"),
    climinclude ("climinclude", mh, "on"),
    aliminclude ("aliminclude", mh, "on")
{
  xdata.set_id (XDATA);
  ydata.set_id (YDATA);
  zdata.set_id (ZDATA);
  cdata.set_id (CDATA);
  cdatamapping.set_id (CDATAMAPPING);
  faces.set_id (FACES);
  facevertexalphadata.set_id (FACEVERTEXALPHADATA);
  facevertexcdata.set_id (FACEVERTEXCDATA);
  vertices.set_id (VERTICES);
  vertexnormals.set_id (VERTEXNORMALS);
  normalmode.set_id (NORMALMODE);
  facecolor.set_id (FACECOLOR);
  facealpha.set_id (FACEALPHA);
  facelighting.set_id (FACELIGHTING);
  edgecolor.set_id (EDGECOLOR);
  edgealpha.set_id (EDGEALPHA);
  edgelighting.set_id (EDGELIGHTING);
  backfacelighting.set_id (BACKFACELIGHTING);
  ambientstrength.set_id (AMBIENTSTRENGTH);
  diffusestrength.set_id (DIFFUSESTRENGTH);
  specularstrength.set_id (SPECULARSTRENGTH);
  specularexponent.set_id (SPECULAREXPONENT);
  specularcolorreflectance.set_id (SPECULARCOLORREFLECTANCE);
  erasemode.set_id (ERASEMODE);
  linestyle.set_id (LINESTYLE);
  linewidth.set_id (LINEWIDTH);
  marker.set_id (MARKER);
  markeredgecolor.set_id (MARKEREDGECOLOR);
  markerfacecolor.set_id (MARKERFACECOLOR);
  markersize.set_id (MARKERSIZE);
  keylabel.set_id (KEYLABEL);
  interpreter.set_id (INTERPRETER);
  alphadatamapping.set_id (ALPHADATAMAPPING);
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  zlim.set_id (ZLIM);
  zlim.set_hidden (true);
  clim.set_id (CLIM);
  clim.set_hidden (true);
  alim.set_id (ALIM);
  alim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  zliminclude.set_id (ZLIMINCLUDE);
  zliminclude.set_hidden (true);
  climinclude.set_id (CLIMINCLUDE);
  climinclude.set_hidden (true);
  aliminclude.set_id (ALIMINCLUDE);
  aliminclude.set_hidden (true);
  init ();
}

void
patch::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("xdata"))
    set_xdata (val);
  else if (pname.compare ("ydata"))
    set_ydata (val);
  else if (pname.compare ("zdata"))
    set_zdata (val);
  else if (pname.compare ("cdata"))
    set_cdata (val);
  else if (pname.compare ("cdatamapping"))
    set_cdatamapping (val);
  else if (pname.compare ("faces"))
    set_faces (val);
  else if (pname.compare ("facevertexalphadata"))
    set_facevertexalphadata (val);
  else if (pname.compare ("facevertexcdata"))
    set_facevertexcdata (val);
  else if (pname.compare ("vertices"))
    set_vertices (val);
  else if (pname.compare ("vertexnormals"))
    set_vertexnormals (val);
  else if (pname.compare ("normalmode"))
    set_normalmode (val);
  else if (pname.compare ("facecolor"))
    set_facecolor (val);
  else if (pname.compare ("facealpha"))
    set_facealpha (val);
  else if (pname.compare ("facelighting"))
    set_facelighting (val);
  else if (pname.compare ("edgecolor"))
    set_edgecolor (val);
  else if (pname.compare ("edgealpha"))
    set_edgealpha (val);
  else if (pname.compare ("edgelighting"))
    set_edgelighting (val);
  else if (pname.compare ("backfacelighting"))
    set_backfacelighting (val);
  else if (pname.compare ("ambientstrength"))
    set_ambientstrength (val);
  else if (pname.compare ("diffusestrength"))
    set_diffusestrength (val);
  else if (pname.compare ("specularstrength"))
    set_specularstrength (val);
  else if (pname.compare ("specularexponent"))
    set_specularexponent (val);
  else if (pname.compare ("specularcolorreflectance"))
    set_specularcolorreflectance (val);
  else if (pname.compare ("erasemode"))
    set_erasemode (val);
  else if (pname.compare ("linestyle"))
    set_linestyle (val);
  else if (pname.compare ("linewidth"))
    set_linewidth (val);
  else if (pname.compare ("marker"))
    set_marker (val);
  else if (pname.compare ("markeredgecolor"))
    set_markeredgecolor (val);
  else if (pname.compare ("markerfacecolor"))
    set_markerfacecolor (val);
  else if (pname.compare ("markersize"))
    set_markersize (val);
  else if (pname.compare ("keylabel"))
    set_keylabel (val);
  else if (pname.compare ("interpreter"))
    set_interpreter (val);
  else if (pname.compare ("alphadatamapping"))
    set_alphadatamapping (val);
  else if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("zliminclude"))
    set_zliminclude (val);
  else if (pname.compare ("climinclude"))
    set_climinclude (val);
  else if (pname.compare ("aliminclude"))
    set_aliminclude (val);
  else
    base_properties::set (pname, "patch", val);
}

octave_value
patch::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("xdata", get_xdata ());
  m.assign ("ydata", get_ydata ());
  m.assign ("zdata", get_zdata ());
  m.assign ("cdata", get_cdata ());
  m.assign ("cdatamapping", get_cdatamapping ());
  m.assign ("faces", get_faces ());
  m.assign ("facevertexalphadata", get_facevertexalphadata ());
  m.assign ("facevertexcdata", get_facevertexcdata ());
  m.assign ("vertices", get_vertices ());
  m.assign ("vertexnormals", get_vertexnormals ());
  m.assign ("normalmode", get_normalmode ());
  m.assign ("facecolor", get_facecolor ());
  m.assign ("facealpha", get_facealpha ());
  m.assign ("facelighting", get_facelighting ());
  m.assign ("edgecolor", get_edgecolor ());
  m.assign ("edgealpha", get_edgealpha ());
  m.assign ("edgelighting", get_edgelighting ());
  m.assign ("backfacelighting", get_backfacelighting ());
  m.assign ("ambientstrength", get_ambientstrength ());
  m.assign ("diffusestrength", get_diffusestrength ());
  m.assign ("specularstrength", get_specularstrength ());
  m.assign ("specularexponent", get_specularexponent ());
  m.assign ("specularcolorreflectance", get_specularcolorreflectance ());
  m.assign ("erasemode", get_erasemode ());
  m.assign ("linestyle", get_linestyle ());
  m.assign ("linewidth", get_linewidth ());
  m.assign ("marker", get_marker ());
  m.assign ("markeredgecolor", get_markeredgecolor ());
  m.assign ("markerfacecolor", get_markerfacecolor ());
  m.assign ("markersize", get_markersize ());
  m.assign ("keylabel", get_keylabel ());
  m.assign ("interpreter", get_interpreter ());
  m.assign ("alphadatamapping", get_alphadatamapping ());
  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("zlim", get_zlim ());
  if (all)
    m.assign ("clim", get_clim ());
  if (all)
    m.assign ("alim", get_alim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("zliminclude", get_zliminclude ());
  if (all)
    m.assign ("climinclude", get_climinclude ());
  if (all)
    m.assign ("aliminclude", get_aliminclude ());

  return m;
}

octave_value
patch::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("xdata"))
    retval = get_xdata ();
  else if (pname.compare ("ydata"))
    retval = get_ydata ();
  else if (pname.compare ("zdata"))
    retval = get_zdata ();
  else if (pname.compare ("cdata"))
    retval = get_cdata ();
  else if (pname.compare ("cdatamapping"))
    retval = get_cdatamapping ();
  else if (pname.compare ("faces"))
    retval = get_faces ();
  else if (pname.compare ("facevertexalphadata"))
    retval = get_facevertexalphadata ();
  else if (pname.compare ("facevertexcdata"))
    retval = get_facevertexcdata ();
  else if (pname.compare ("vertices"))
    retval = get_vertices ();
  else if (pname.compare ("vertexnormals"))
    retval = get_vertexnormals ();
  else if (pname.compare ("normalmode"))
    retval = get_normalmode ();
  else if (pname.compare ("facecolor"))
    retval = get_facecolor ();
  else if (pname.compare ("facealpha"))
    retval = get_facealpha ();
  else if (pname.compare ("facelighting"))
    retval = get_facelighting ();
  else if (pname.compare ("edgecolor"))
    retval = get_edgecolor ();
  else if (pname.compare ("edgealpha"))
    retval = get_edgealpha ();
  else if (pname.compare ("edgelighting"))
    retval = get_edgelighting ();
  else if (pname.compare ("backfacelighting"))
    retval = get_backfacelighting ();
  else if (pname.compare ("ambientstrength"))
    retval = get_ambientstrength ();
  else if (pname.compare ("diffusestrength"))
    retval = get_diffusestrength ();
  else if (pname.compare ("specularstrength"))
    retval = get_specularstrength ();
  else if (pname.compare ("specularexponent"))
    retval = get_specularexponent ();
  else if (pname.compare ("specularcolorreflectance"))
    retval = get_specularcolorreflectance ();
  else if (pname.compare ("erasemode"))
    retval = get_erasemode ();
  else if (pname.compare ("linestyle"))
    retval = get_linestyle ();
  else if (pname.compare ("linewidth"))
    retval = get_linewidth ();
  else if (pname.compare ("marker"))
    retval = get_marker ();
  else if (pname.compare ("markeredgecolor"))
    retval = get_markeredgecolor ();
  else if (pname.compare ("markerfacecolor"))
    retval = get_markerfacecolor ();
  else if (pname.compare ("markersize"))
    retval = get_markersize ();
  else if (pname.compare ("keylabel"))
    retval = get_keylabel ();
  else if (pname.compare ("interpreter"))
    retval = get_interpreter ();
  else if (pname.compare ("alphadatamapping"))
    retval = get_alphadatamapping ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("clim"))
    retval = get_clim ();
  else if (pname.compare ("alim"))
    retval = get_alim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("zliminclude"))
    retval = get_zliminclude ();
  else if (pname.compare ("climinclude"))
    retval = get_climinclude ();
  else if (pname.compare ("aliminclude"))
    retval = get_aliminclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
patch::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("xdata"))
    return property (&xdata, true);
  else if (pname.compare ("ydata"))
    return property (&ydata, true);
  else if (pname.compare ("zdata"))
    return property (&zdata, true);
  else if (pname.compare ("cdata"))
    return property (&cdata, true);
  else if (pname.compare ("cdatamapping"))
    return property (&cdatamapping, true);
  else if (pname.compare ("faces"))
    return property (&faces, true);
  else if (pname.compare ("facevertexalphadata"))
    return property (&facevertexalphadata, true);
  else if (pname.compare ("facevertexcdata"))
    return property (&facevertexcdata, true);
  else if (pname.compare ("vertices"))
    return property (&vertices, true);
  else if (pname.compare ("vertexnormals"))
    return property (&vertexnormals, true);
  else if (pname.compare ("normalmode"))
    return property (&normalmode, true);
  else if (pname.compare ("facecolor"))
    return property (&facecolor, true);
  else if (pname.compare ("facealpha"))
    return property (&facealpha, true);
  else if (pname.compare ("facelighting"))
    return property (&facelighting, true);
  else if (pname.compare ("edgecolor"))
    return property (&edgecolor, true);
  else if (pname.compare ("edgealpha"))
    return property (&edgealpha, true);
  else if (pname.compare ("edgelighting"))
    return property (&edgelighting, true);
  else if (pname.compare ("backfacelighting"))
    return property (&backfacelighting, true);
  else if (pname.compare ("ambientstrength"))
    return property (&ambientstrength, true);
  else if (pname.compare ("diffusestrength"))
    return property (&diffusestrength, true);
  else if (pname.compare ("specularstrength"))
    return property (&specularstrength, true);
  else if (pname.compare ("specularexponent"))
    return property (&specularexponent, true);
  else if (pname.compare ("specularcolorreflectance"))
    return property (&specularcolorreflectance, true);
  else if (pname.compare ("erasemode"))
    return property (&erasemode, true);
  else if (pname.compare ("linestyle"))
    return property (&linestyle, true);
  else if (pname.compare ("linewidth"))
    return property (&linewidth, true);
  else if (pname.compare ("marker"))
    return property (&marker, true);
  else if (pname.compare ("markeredgecolor"))
    return property (&markeredgecolor, true);
  else if (pname.compare ("markerfacecolor"))
    return property (&markerfacecolor, true);
  else if (pname.compare ("markersize"))
    return property (&markersize, true);
  else if (pname.compare ("keylabel"))
    return property (&keylabel, true);
  else if (pname.compare ("interpreter"))
    return property (&interpreter, true);
  else if (pname.compare ("alphadatamapping"))
    return property (&alphadatamapping, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("clim"))
    return property (&clim, true);
  else if (pname.compare ("alim"))
    return property (&alim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("zliminclude"))
    return property (&zliminclude, true);
  else if (pname.compare ("climinclude"))
    return property (&climinclude, true);
  else if (pname.compare ("aliminclude"))
    return property (&aliminclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
patch::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["xdata"] = Matrix ();
  m["ydata"] = Matrix ();
  m["zdata"] = Matrix ();
  m["cdata"] = Matrix ();
  m["cdatamapping"] = "scaled";
  m["faces"] = Matrix ();
  m["facevertexalphadata"] = Matrix ();
  m["facevertexcdata"] = Matrix ();
  m["vertices"] = Matrix ();
  m["vertexnormals"] = Matrix ();
  m["normalmode"] = "auto";
  m["facecolor"] = "flat";
  m["facealpha"] = double_radio_property (1.0, radio_values ("flat|interp"));
  m["facelighting"] = "none";
  m["edgecolor"] = octave_value ();
  m["edgealpha"] = double_radio_property (1.0, radio_values ("flat|interp"));
  m["edgelighting"] = "none";
  m["backfacelighting"] = "reverselit";
  m["ambientstrength"] = 0.3;
  m["diffusestrength"] = 0.6;
  m["specularstrength"] = 0.6;
  m["specularexponent"] = 10.0;
  m["specularcolorreflectance"] = 1.0;
  m["erasemode"] = "normal";
  m["linestyle"] = "-";
  m["linewidth"] = 0.5;
  m["marker"] = "none";
  m["markeredgecolor"] = "auto";
  m["markerfacecolor"] = "none";
  m["markersize"] = 6;
  m["keylabel"] = "";
  m["interpreter"] = "tex";
  m["alphadatamapping"] = "scaled";
  m["xlim"] = Matrix ();
  m["ylim"] = Matrix ();
  m["zlim"] = Matrix ();
  m["clim"] = Matrix ();
  m["alim"] = Matrix ();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["zliminclude"] = "on";
  m["climinclude"] = "on";
  m["aliminclude"] = "on";

  return m;
}

std::string patch::properties::go_name ("patch");

bool patch::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("xdata");
      all_properties.insert ("ydata");
      all_properties.insert ("zdata");
      all_properties.insert ("cdata");
      all_properties.insert ("cdatamapping");
      all_properties.insert ("faces");
      all_properties.insert ("facevertexalphadata");
      all_properties.insert ("facevertexcdata");
      all_properties.insert ("vertices");
      all_properties.insert ("vertexnormals");
      all_properties.insert ("normalmode");
      all_properties.insert ("facecolor");
      all_properties.insert ("facealpha");
      all_properties.insert ("facelighting");
      all_properties.insert ("edgecolor");
      all_properties.insert ("edgealpha");
      all_properties.insert ("edgelighting");
      all_properties.insert ("backfacelighting");
      all_properties.insert ("ambientstrength");
      all_properties.insert ("diffusestrength");
      all_properties.insert ("specularstrength");
      all_properties.insert ("specularexponent");
      all_properties.insert ("specularcolorreflectance");
      all_properties.insert ("erasemode");
      all_properties.insert ("linestyle");
      all_properties.insert ("linewidth");
      all_properties.insert ("marker");
      all_properties.insert ("markeredgecolor");
      all_properties.insert ("markerfacecolor");
      all_properties.insert ("markersize");
      all_properties.insert ("keylabel");
      all_properties.insert ("interpreter");
      all_properties.insert ("alphadatamapping");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("clim");
      all_properties.insert ("alim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("zliminclude");
      all_properties.insert ("climinclude");
      all_properties.insert ("aliminclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "patch");
}

// ******** surface ********

surface::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    xdata ("xdata", mh, Matrix ()),
    ydata ("ydata", mh, Matrix ()),
    zdata ("zdata", mh, Matrix ()),
    cdata ("cdata", mh, Matrix ()),
    cdatamapping ("cdatamapping", mh, "{scaled}|direct"),
    xdatasource ("xdatasource", mh, ""),
    ydatasource ("ydatasource", mh, ""),
    zdatasource ("zdatasource", mh, ""),
    cdatasource ("cdatasource", mh, ""),
    facecolor ("facecolor", mh, "{flat}|none|interp|texturemap"),
    facealpha ("facealpha", mh, double_radio_property (1.0, radio_values ("flat|interp"))),
    edgecolor ("edgecolor", mh, color_property (color_values (0, 0, 0), radio_values ("flat|none|interp"))),
    linestyle ("linestyle", mh, "{-}|--|:|-.|none"),
    linewidth ("linewidth", mh, 0.5),
    marker ("marker", mh, "{none}|s|o|x|+|.|*|<|>|v|^|d|p|h"),
    markeredgecolor ("markeredgecolor", mh, "{auto}|none"),
    markerfacecolor ("markerfacecolor", mh, "auto|{none}"),
    markersize ("markersize", mh, 6),
    keylabel ("keylabel", mh, ""),
    interpreter ("interpreter", mh, "{tex}|none|latex"),
    alphadata ("alphadata", mh, Matrix ()),
    alphadatamapping ("alphadatamapping", mh, "none|direct|{scaled}"),
    ambientstrength ("ambientstrength", mh, 0.3),
    backfacelighting ("backfacelighting", mh, "unlit|lit|{reverselit}"),
    diffusestrength ("diffusestrength", mh, 0.6),
    edgealpha ("edgealpha", mh, double_radio_property (1.0, radio_values ("flat|interp"))),
    edgelighting ("edgelighting", mh, "{none}|flat|gouraud|phong"),
    erasemode ("erasemode", mh, "{normal}|none|xor|background"),
    facelighting ("facelighting", mh, "{none}|flat|gouraud|phong"),
    meshstyle ("meshstyle", mh, "{both}|row|column"),
    normalmode ("normalmode", mh, "{auto}|manual"),
    specularcolorreflectance ("specularcolorreflectance", mh, 1),
    specularexponent ("specularexponent", mh, 10),
    specularstrength ("specularstrength", mh, 0.9),
    vertexnormals ("vertexnormals", mh, Matrix ()),
    xlim ("xlim", mh, Matrix ()),
    ylim ("ylim", mh, Matrix ()),
    zlim ("zlim", mh, Matrix ()),
    clim ("clim", mh, Matrix ()),
    alim ("alim", mh, Matrix ()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    zliminclude ("zliminclude", mh, "on"),
    climinclude ("climinclude", mh, "on"),
    aliminclude ("aliminclude", mh, "on")
{
  xdata.set_id (XDATA);
  ydata.set_id (YDATA);
  zdata.set_id (ZDATA);
  cdata.set_id (CDATA);
  cdatamapping.set_id (CDATAMAPPING);
  xdatasource.set_id (XDATASOURCE);
  ydatasource.set_id (YDATASOURCE);
  zdatasource.set_id (ZDATASOURCE);
  cdatasource.set_id (CDATASOURCE);
  facecolor.set_id (FACECOLOR);
  facealpha.set_id (FACEALPHA);
  edgecolor.set_id (EDGECOLOR);
  linestyle.set_id (LINESTYLE);
  linewidth.set_id (LINEWIDTH);
  marker.set_id (MARKER);
  markeredgecolor.set_id (MARKEREDGECOLOR);
  markerfacecolor.set_id (MARKERFACECOLOR);
  markersize.set_id (MARKERSIZE);
  keylabel.set_id (KEYLABEL);
  interpreter.set_id (INTERPRETER);
  alphadata.set_id (ALPHADATA);
  alphadatamapping.set_id (ALPHADATAMAPPING);
  ambientstrength.set_id (AMBIENTSTRENGTH);
  backfacelighting.set_id (BACKFACELIGHTING);
  diffusestrength.set_id (DIFFUSESTRENGTH);
  edgealpha.set_id (EDGEALPHA);
  edgelighting.set_id (EDGELIGHTING);
  erasemode.set_id (ERASEMODE);
  facelighting.set_id (FACELIGHTING);
  meshstyle.set_id (MESHSTYLE);
  normalmode.set_id (NORMALMODE);
  specularcolorreflectance.set_id (SPECULARCOLORREFLECTANCE);
  specularexponent.set_id (SPECULAREXPONENT);
  specularstrength.set_id (SPECULARSTRENGTH);
  vertexnormals.set_id (VERTEXNORMALS);
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  zlim.set_id (ZLIM);
  zlim.set_hidden (true);
  clim.set_id (CLIM);
  clim.set_hidden (true);
  alim.set_id (ALIM);
  alim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  zliminclude.set_id (ZLIMINCLUDE);
  zliminclude.set_hidden (true);
  climinclude.set_id (CLIMINCLUDE);
  climinclude.set_hidden (true);
  aliminclude.set_id (ALIMINCLUDE);
  aliminclude.set_hidden (true);
  init ();
}

void
surface::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("xdata"))
    set_xdata (val);
  else if (pname.compare ("ydata"))
    set_ydata (val);
  else if (pname.compare ("zdata"))
    set_zdata (val);
  else if (pname.compare ("cdata"))
    set_cdata (val);
  else if (pname.compare ("cdatamapping"))
    set_cdatamapping (val);
  else if (pname.compare ("xdatasource"))
    set_xdatasource (val);
  else if (pname.compare ("ydatasource"))
    set_ydatasource (val);
  else if (pname.compare ("zdatasource"))
    set_zdatasource (val);
  else if (pname.compare ("cdatasource"))
    set_cdatasource (val);
  else if (pname.compare ("facecolor"))
    set_facecolor (val);
  else if (pname.compare ("facealpha"))
    set_facealpha (val);
  else if (pname.compare ("edgecolor"))
    set_edgecolor (val);
  else if (pname.compare ("linestyle"))
    set_linestyle (val);
  else if (pname.compare ("linewidth"))
    set_linewidth (val);
  else if (pname.compare ("marker"))
    set_marker (val);
  else if (pname.compare ("markeredgecolor"))
    set_markeredgecolor (val);
  else if (pname.compare ("markerfacecolor"))
    set_markerfacecolor (val);
  else if (pname.compare ("markersize"))
    set_markersize (val);
  else if (pname.compare ("keylabel"))
    set_keylabel (val);
  else if (pname.compare ("interpreter"))
    set_interpreter (val);
  else if (pname.compare ("alphadata"))
    set_alphadata (val);
  else if (pname.compare ("alphadatamapping"))
    set_alphadatamapping (val);
  else if (pname.compare ("ambientstrength"))
    set_ambientstrength (val);
  else if (pname.compare ("backfacelighting"))
    set_backfacelighting (val);
  else if (pname.compare ("diffusestrength"))
    set_diffusestrength (val);
  else if (pname.compare ("edgealpha"))
    set_edgealpha (val);
  else if (pname.compare ("edgelighting"))
    set_edgelighting (val);
  else if (pname.compare ("erasemode"))
    set_erasemode (val);
  else if (pname.compare ("facelighting"))
    set_facelighting (val);
  else if (pname.compare ("meshstyle"))
    set_meshstyle (val);
  else if (pname.compare ("normalmode"))
    set_normalmode (val);
  else if (pname.compare ("specularcolorreflectance"))
    set_specularcolorreflectance (val);
  else if (pname.compare ("specularexponent"))
    set_specularexponent (val);
  else if (pname.compare ("specularstrength"))
    set_specularstrength (val);
  else if (pname.compare ("vertexnormals"))
    set_vertexnormals (val);
  else if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("zliminclude"))
    set_zliminclude (val);
  else if (pname.compare ("climinclude"))
    set_climinclude (val);
  else if (pname.compare ("aliminclude"))
    set_aliminclude (val);
  else
    base_properties::set (pname, "surface", val);
}

octave_value
surface::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  m.assign ("xdata", get_xdata ());
  m.assign ("ydata", get_ydata ());
  m.assign ("zdata", get_zdata ());
  m.assign ("cdata", get_cdata ());
  m.assign ("cdatamapping", get_cdatamapping ());
  m.assign ("xdatasource", get_xdatasource ());
  m.assign ("ydatasource", get_ydatasource ());
  m.assign ("zdatasource", get_zdatasource ());
  m.assign ("cdatasource", get_cdatasource ());
  m.assign ("facecolor", get_facecolor ());
  m.assign ("facealpha", get_facealpha ());
  m.assign ("edgecolor", get_edgecolor ());
  m.assign ("linestyle", get_linestyle ());
  m.assign ("linewidth", get_linewidth ());
  m.assign ("marker", get_marker ());
  m.assign ("markeredgecolor", get_markeredgecolor ());
  m.assign ("markerfacecolor", get_markerfacecolor ());
  m.assign ("markersize", get_markersize ());
  m.assign ("keylabel", get_keylabel ());
  m.assign ("interpreter", get_interpreter ());
  m.assign ("alphadata", get_alphadata ());
  m.assign ("alphadatamapping", get_alphadatamapping ());
  m.assign ("ambientstrength", get_ambientstrength ());
  m.assign ("backfacelighting", get_backfacelighting ());
  m.assign ("diffusestrength", get_diffusestrength ());
  m.assign ("edgealpha", get_edgealpha ());
  m.assign ("edgelighting", get_edgelighting ());
  m.assign ("erasemode", get_erasemode ());
  m.assign ("facelighting", get_facelighting ());
  m.assign ("meshstyle", get_meshstyle ());
  m.assign ("normalmode", get_normalmode ());
  m.assign ("specularcolorreflectance", get_specularcolorreflectance ());
  m.assign ("specularexponent", get_specularexponent ());
  m.assign ("specularstrength", get_specularstrength ());
  m.assign ("vertexnormals", get_vertexnormals ());
  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("zlim", get_zlim ());
  if (all)
    m.assign ("clim", get_clim ());
  if (all)
    m.assign ("alim", get_alim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("zliminclude", get_zliminclude ());
  if (all)
    m.assign ("climinclude", get_climinclude ());
  if (all)
    m.assign ("aliminclude", get_aliminclude ());

  return m;
}

octave_value
surface::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("xdata"))
    retval = get_xdata ();
  else if (pname.compare ("ydata"))
    retval = get_ydata ();
  else if (pname.compare ("zdata"))
    retval = get_zdata ();
  else if (pname.compare ("cdata"))
    retval = get_cdata ();
  else if (pname.compare ("cdatamapping"))
    retval = get_cdatamapping ();
  else if (pname.compare ("xdatasource"))
    retval = get_xdatasource ();
  else if (pname.compare ("ydatasource"))
    retval = get_ydatasource ();
  else if (pname.compare ("zdatasource"))
    retval = get_zdatasource ();
  else if (pname.compare ("cdatasource"))
    retval = get_cdatasource ();
  else if (pname.compare ("facecolor"))
    retval = get_facecolor ();
  else if (pname.compare ("facealpha"))
    retval = get_facealpha ();
  else if (pname.compare ("edgecolor"))
    retval = get_edgecolor ();
  else if (pname.compare ("linestyle"))
    retval = get_linestyle ();
  else if (pname.compare ("linewidth"))
    retval = get_linewidth ();
  else if (pname.compare ("marker"))
    retval = get_marker ();
  else if (pname.compare ("markeredgecolor"))
    retval = get_markeredgecolor ();
  else if (pname.compare ("markerfacecolor"))
    retval = get_markerfacecolor ();
  else if (pname.compare ("markersize"))
    retval = get_markersize ();
  else if (pname.compare ("keylabel"))
    retval = get_keylabel ();
  else if (pname.compare ("interpreter"))
    retval = get_interpreter ();
  else if (pname.compare ("alphadata"))
    retval = get_alphadata ();
  else if (pname.compare ("alphadatamapping"))
    retval = get_alphadatamapping ();
  else if (pname.compare ("ambientstrength"))
    retval = get_ambientstrength ();
  else if (pname.compare ("backfacelighting"))
    retval = get_backfacelighting ();
  else if (pname.compare ("diffusestrength"))
    retval = get_diffusestrength ();
  else if (pname.compare ("edgealpha"))
    retval = get_edgealpha ();
  else if (pname.compare ("edgelighting"))
    retval = get_edgelighting ();
  else if (pname.compare ("erasemode"))
    retval = get_erasemode ();
  else if (pname.compare ("facelighting"))
    retval = get_facelighting ();
  else if (pname.compare ("meshstyle"))
    retval = get_meshstyle ();
  else if (pname.compare ("normalmode"))
    retval = get_normalmode ();
  else if (pname.compare ("specularcolorreflectance"))
    retval = get_specularcolorreflectance ();
  else if (pname.compare ("specularexponent"))
    retval = get_specularexponent ();
  else if (pname.compare ("specularstrength"))
    retval = get_specularstrength ();
  else if (pname.compare ("vertexnormals"))
    retval = get_vertexnormals ();
  else if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("clim"))
    retval = get_clim ();
  else if (pname.compare ("alim"))
    retval = get_alim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("zliminclude"))
    retval = get_zliminclude ();
  else if (pname.compare ("climinclude"))
    retval = get_climinclude ();
  else if (pname.compare ("aliminclude"))
    retval = get_aliminclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
surface::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("xdata"))
    return property (&xdata, true);
  else if (pname.compare ("ydata"))
    return property (&ydata, true);
  else if (pname.compare ("zdata"))
    return property (&zdata, true);
  else if (pname.compare ("cdata"))
    return property (&cdata, true);
  else if (pname.compare ("cdatamapping"))
    return property (&cdatamapping, true);
  else if (pname.compare ("xdatasource"))
    return property (&xdatasource, true);
  else if (pname.compare ("ydatasource"))
    return property (&ydatasource, true);
  else if (pname.compare ("zdatasource"))
    return property (&zdatasource, true);
  else if (pname.compare ("cdatasource"))
    return property (&cdatasource, true);
  else if (pname.compare ("facecolor"))
    return property (&facecolor, true);
  else if (pname.compare ("facealpha"))
    return property (&facealpha, true);
  else if (pname.compare ("edgecolor"))
    return property (&edgecolor, true);
  else if (pname.compare ("linestyle"))
    return property (&linestyle, true);
  else if (pname.compare ("linewidth"))
    return property (&linewidth, true);
  else if (pname.compare ("marker"))
    return property (&marker, true);
  else if (pname.compare ("markeredgecolor"))
    return property (&markeredgecolor, true);
  else if (pname.compare ("markerfacecolor"))
    return property (&markerfacecolor, true);
  else if (pname.compare ("markersize"))
    return property (&markersize, true);
  else if (pname.compare ("keylabel"))
    return property (&keylabel, true);
  else if (pname.compare ("interpreter"))
    return property (&interpreter, true);
  else if (pname.compare ("alphadata"))
    return property (&alphadata, true);
  else if (pname.compare ("alphadatamapping"))
    return property (&alphadatamapping, true);
  else if (pname.compare ("ambientstrength"))
    return property (&ambientstrength, true);
  else if (pname.compare ("backfacelighting"))
    return property (&backfacelighting, true);
  else if (pname.compare ("diffusestrength"))
    return property (&diffusestrength, true);
  else if (pname.compare ("edgealpha"))
    return property (&edgealpha, true);
  else if (pname.compare ("edgelighting"))
    return property (&edgelighting, true);
  else if (pname.compare ("erasemode"))
    return property (&erasemode, true);
  else if (pname.compare ("facelighting"))
    return property (&facelighting, true);
  else if (pname.compare ("meshstyle"))
    return property (&meshstyle, true);
  else if (pname.compare ("normalmode"))
    return property (&normalmode, true);
  else if (pname.compare ("specularcolorreflectance"))
    return property (&specularcolorreflectance, true);
  else if (pname.compare ("specularexponent"))
    return property (&specularexponent, true);
  else if (pname.compare ("specularstrength"))
    return property (&specularstrength, true);
  else if (pname.compare ("vertexnormals"))
    return property (&vertexnormals, true);
  else if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("clim"))
    return property (&clim, true);
  else if (pname.compare ("alim"))
    return property (&alim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("zliminclude"))
    return property (&zliminclude, true);
  else if (pname.compare ("climinclude"))
    return property (&climinclude, true);
  else if (pname.compare ("aliminclude"))
    return property (&aliminclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
surface::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["xdata"] = Matrix ();
  m["ydata"] = Matrix ();
  m["zdata"] = Matrix ();
  m["cdata"] = Matrix ();
  m["cdatamapping"] = "scaled";
  m["xdatasource"] = "";
  m["ydatasource"] = "";
  m["zdatasource"] = "";
  m["cdatasource"] = "";
  m["facecolor"] = "flat";
  m["facealpha"] = double_radio_property (1.0, radio_values ("flat|interp"));
  m["edgecolor"] = octave_value ();
  m["linestyle"] = "-";
  m["linewidth"] = 0.5;
  m["marker"] = "none";
  m["markeredgecolor"] = "auto";
  m["markerfacecolor"] = "none";
  m["markersize"] = 6;
  m["keylabel"] = "";
  m["interpreter"] = "tex";
  m["alphadata"] = Matrix ();
  m["alphadatamapping"] = "scaled";
  m["ambientstrength"] = 0.3;
  m["backfacelighting"] = "reverselit";
  m["diffusestrength"] = 0.6;
  m["edgealpha"] = double_radio_property (1.0, radio_values ("flat|interp"));
  m["edgelighting"] = "none";
  m["erasemode"] = "normal";
  m["facelighting"] = "none";
  m["meshstyle"] = "both";
  m["normalmode"] = "auto";
  m["specularcolorreflectance"] = 1;
  m["specularexponent"] = 10;
  m["specularstrength"] = 0.9;
  m["vertexnormals"] = Matrix ();
  m["xlim"] = Matrix ();
  m["ylim"] = Matrix ();
  m["zlim"] = Matrix ();
  m["clim"] = Matrix ();
  m["alim"] = Matrix ();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["zliminclude"] = "on";
  m["climinclude"] = "on";
  m["aliminclude"] = "on";

  return m;
}

std::string surface::properties::go_name ("surface");

bool surface::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("xdata");
      all_properties.insert ("ydata");
      all_properties.insert ("zdata");
      all_properties.insert ("cdata");
      all_properties.insert ("cdatamapping");
      all_properties.insert ("xdatasource");
      all_properties.insert ("ydatasource");
      all_properties.insert ("zdatasource");
      all_properties.insert ("cdatasource");
      all_properties.insert ("facecolor");
      all_properties.insert ("facealpha");
      all_properties.insert ("edgecolor");
      all_properties.insert ("linestyle");
      all_properties.insert ("linewidth");
      all_properties.insert ("marker");
      all_properties.insert ("markeredgecolor");
      all_properties.insert ("markerfacecolor");
      all_properties.insert ("markersize");
      all_properties.insert ("keylabel");
      all_properties.insert ("interpreter");
      all_properties.insert ("alphadata");
      all_properties.insert ("alphadatamapping");
      all_properties.insert ("ambientstrength");
      all_properties.insert ("backfacelighting");
      all_properties.insert ("diffusestrength");
      all_properties.insert ("edgealpha");
      all_properties.insert ("edgelighting");
      all_properties.insert ("erasemode");
      all_properties.insert ("facelighting");
      all_properties.insert ("meshstyle");
      all_properties.insert ("normalmode");
      all_properties.insert ("specularcolorreflectance");
      all_properties.insert ("specularexponent");
      all_properties.insert ("specularstrength");
      all_properties.insert ("vertexnormals");
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("clim");
      all_properties.insert ("alim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("zliminclude");
      all_properties.insert ("climinclude");
      all_properties.insert ("aliminclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "surface");
}

// ******** hggroup ********

hggroup::properties::properties (const graphics_handle& mh, const graphics_handle& p)
  : base_properties (go_name, mh, p),
    xlim ("xlim", mh, Matrix()),
    ylim ("ylim", mh, Matrix()),
    zlim ("zlim", mh, Matrix()),
    clim ("clim", mh, Matrix()),
    alim ("alim", mh, Matrix()),
    xliminclude ("xliminclude", mh, "on"),
    yliminclude ("yliminclude", mh, "on"),
    zliminclude ("zliminclude", mh, "on"),
    climinclude ("climinclude", mh, "on"),
    aliminclude ("aliminclude", mh, "on")
{
  xlim.set_id (XLIM);
  xlim.set_hidden (true);
  ylim.set_id (YLIM);
  ylim.set_hidden (true);
  zlim.set_id (ZLIM);
  zlim.set_hidden (true);
  clim.set_id (CLIM);
  clim.set_hidden (true);
  alim.set_id (ALIM);
  alim.set_hidden (true);
  xliminclude.set_id (XLIMINCLUDE);
  xliminclude.set_hidden (true);
  yliminclude.set_id (YLIMINCLUDE);
  yliminclude.set_hidden (true);
  zliminclude.set_id (ZLIMINCLUDE);
  zliminclude.set_hidden (true);
  climinclude.set_id (CLIMINCLUDE);
  climinclude.set_hidden (true);
  aliminclude.set_id (ALIMINCLUDE);
  aliminclude.set_hidden (true);
  init ();
}

void
hggroup::properties::set (const caseless_str& pname, const octave_value& val)
{
  if (pname.compare ("xliminclude"))
    set_xliminclude (val);
  else if (pname.compare ("yliminclude"))
    set_yliminclude (val);
  else if (pname.compare ("zliminclude"))
    set_zliminclude (val);
  else if (pname.compare ("climinclude"))
    set_climinclude (val);
  else if (pname.compare ("aliminclude"))
    set_aliminclude (val);
  else
    base_properties::set (pname, "hggroup", val);
}

octave_value
hggroup::properties::get (bool all) const
{
  Octave_map m = base_properties::get (all).map_value ();

  if (all)
    m.assign ("xlim", get_xlim ());
  if (all)
    m.assign ("ylim", get_ylim ());
  if (all)
    m.assign ("zlim", get_zlim ());
  if (all)
    m.assign ("clim", get_clim ());
  if (all)
    m.assign ("alim", get_alim ());
  if (all)
    m.assign ("xliminclude", get_xliminclude ());
  if (all)
    m.assign ("yliminclude", get_yliminclude ());
  if (all)
    m.assign ("zliminclude", get_zliminclude ());
  if (all)
    m.assign ("climinclude", get_climinclude ());
  if (all)
    m.assign ("aliminclude", get_aliminclude ());

  return m;
}

octave_value
hggroup::properties::get (const caseless_str& pname) const
{
  octave_value retval;

  if (pname.compare ("xlim"))
    retval = get_xlim ();
  else if (pname.compare ("ylim"))
    retval = get_ylim ();
  else if (pname.compare ("zlim"))
    retval = get_zlim ();
  else if (pname.compare ("clim"))
    retval = get_clim ();
  else if (pname.compare ("alim"))
    retval = get_alim ();
  else if (pname.compare ("xliminclude"))
    retval = get_xliminclude ();
  else if (pname.compare ("yliminclude"))
    retval = get_yliminclude ();
  else if (pname.compare ("zliminclude"))
    retval = get_zliminclude ();
  else if (pname.compare ("climinclude"))
    retval = get_climinclude ();
  else if (pname.compare ("aliminclude"))
    retval = get_aliminclude ();
  else
    retval = base_properties::get (pname);

  return retval;
}

property
hggroup::properties::get_property (const caseless_str& pname)
{
  if (pname.compare ("xlim"))
    return property (&xlim, true);
  else if (pname.compare ("ylim"))
    return property (&ylim, true);
  else if (pname.compare ("zlim"))
    return property (&zlim, true);
  else if (pname.compare ("clim"))
    return property (&clim, true);
  else if (pname.compare ("alim"))
    return property (&alim, true);
  else if (pname.compare ("xliminclude"))
    return property (&xliminclude, true);
  else if (pname.compare ("yliminclude"))
    return property (&yliminclude, true);
  else if (pname.compare ("zliminclude"))
    return property (&zliminclude, true);
  else if (pname.compare ("climinclude"))
    return property (&climinclude, true);
  else if (pname.compare ("aliminclude"))
    return property (&aliminclude, true);
  else
    return base_properties::get_property (pname);
}

property_list::pval_map_type
hggroup::properties::factory_defaults (void)
{
  property_list::pval_map_type m = base_properties::factory_defaults ();

  m["xlim"] = Matrix();
  m["ylim"] = Matrix();
  m["zlim"] = Matrix();
  m["clim"] = Matrix();
  m["alim"] = Matrix();
  m["xliminclude"] = "on";
  m["yliminclude"] = "on";
  m["zliminclude"] = "on";
  m["climinclude"] = "on";
  m["aliminclude"] = "on";

  return m;
}

std::string hggroup::properties::go_name ("hggroup");

bool hggroup::properties::has_property (const std::string& pname)
{
  static std::set<std::string> all_properties;

  static bool initialized = false;

  if (! initialized)
    {
      all_properties.insert ("xlim");
      all_properties.insert ("ylim");
      all_properties.insert ("zlim");
      all_properties.insert ("clim");
      all_properties.insert ("alim");
      all_properties.insert ("xliminclude");
      all_properties.insert ("yliminclude");
      all_properties.insert ("zliminclude");
      all_properties.insert ("climinclude");
      all_properties.insert ("aliminclude");

      initialized = true;
    }

  return all_properties.find (pname) != all_properties.end () || base_properties::has_property (pname, "hggroup");
}

