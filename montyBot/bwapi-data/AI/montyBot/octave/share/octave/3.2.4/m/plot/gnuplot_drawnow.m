## Copyright (C) 2005, 2006, 2007, 2008, 2009 John W. Eaton
##
## This file is part of Octave.
##
## Octave is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or (at
## your option) any later version.
##
## Octave is distributed in the hope that it will be useful, but
## WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
## General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {Function File} {} drawnow ()
## Update and display the current graphics.
##
## Octave automatically calls drawnow just before printing a prompt,
## when @code{sleep} or @code{pause} is called, or while waiting for
## command-line input.
## @end deftypefn

## Author: jwe

function gnuplot_drawnow (h, term, file, mono, debug_file)

  if (nargin < 4)
    mono = false;
  endif

  if (nargin >= 3 && nargin <= 5)
    ## Produce various output formats, or redirect gnuplot stream to a
    ## debug file.
    plot_stream = [];
    fid = [];
    printing = ! output_to_screen (gnuplot_trim_term (term));
    default_plot_stream = get (h, "__plot_stream__");
    unwind_protect
      plot_stream = __gnuplot_open_stream__ (2, h);
      if (__gnuplot_has_feature__ ("variable_GPVAL_TERMINALS"))
        available_terminals = __gnuplot_get_var__ (plot_stream, "GPVAL_TERMINALS");
        available_terminals = regexp (available_terminals, "\\b\\w+\\b", "match");
        gnuplot_supports_term = any (strcmpi (available_terminals,
                                              gnuplot_trim_term (term)));
      else
        gnuplot_supports_term = true;
      endif
      if (gnuplot_supports_term)
        [enhanced, implicit_margin] = gnuplot_set_term (plot_stream (1), true,
                                                        h, term, file);
        __go_draw_figure__ (h, plot_stream(1), enhanced, mono, printing, implicit_margin);
        if (nargin == 5)
          fid = fopen (debug_file, "wb");
          [enhanced, implicit_margin] = gnuplot_set_term (fid, true, h, term, file);
          __go_draw_figure__ (h, fid, enhanced, mono, printing, implicit_margin);
        endif
      else
        error ("gnuplot_drawnow: the gnuplot terminal, \"%s\", is not available.",
               gnuplot_trim_term (term))
      endif
    unwind_protect_cleanup
      set (h, "__plot_stream__", default_plot_stream);
      if (! isempty (plot_stream))
        pclose (plot_stream(1));
        if (numel (plot_stream) > 1)
          pclose (plot_stream(2));
        endif
	if (numel (plot_stream) > 2)
	  waitpid (plot_stream(3));
	endif
      endif
      if (! isempty (fid))
        fclose (fid);
      endif
    end_unwind_protect
  elseif (nargin == 1)
    ##  Graphics terminal for display.
    plot_stream = get (h, "__plot_stream__");
    if (isempty (plot_stream))
      plot_stream = __gnuplot_open_stream__ (2, h);
      new_stream = true;
    else
      new_stream = false;
    endif
    term = gnuplot_default_term ();
    if (strcmp (term, "dumb"))
      ## popen2 eats stdout of gnuplot, use temporary file instead
      dumb_tmp_file = tmpnam ();
      enhanced = gnuplot_set_term (plot_stream (1), new_stream, h, ...
                                   term, dumb_tmp_file);
    else
      enhanced = gnuplot_set_term (plot_stream (1), new_stream, h, term);
    end
    __go_draw_figure__ (h, plot_stream (1), enhanced, mono, 0);
    fflush (plot_stream (1));
    if (strcmp (term, "dumb"))
      fid = -1;
      while (fid < 0)
        pause (0.1);
        fid = fopen (dumb_tmp_file, 'r');
      endwhile
      ## reprint the plot on screen
      [a, count] = fscanf (fid, '%c', Inf);
      puts (a);
      fclose (fid);
      unlink (dumb_tmp_file);
    endif
  else
    print_usage ();
  endif

endfunction

function implicit_margin = gnuplot_implicit_margin (term, opts_str)
  ## gnuplot has an implicit margin of 50pts for PS output.
  if (strcmpi (term, "postscript"))
    if (isempty (strfind (opts_str, " eps"))
        && isempty (strfind (opts_str, "eps ")))
      implicit_margin = 50/72;
    else
      ## When zero, the behavior of gnuplot changes.
      implicit_margin = 1/72;
    endif
  else
    implicit_margin = 0.0;
  endif
endfunction

function [enhanced, implicit_margin] = gnuplot_set_term (plot_stream, new_stream, h, term, file)
  ## Generate the gnuplot "set terminal <term> ..." command.
  ## When "term" originates from print.m, it may include other options.
  if (nargin < 4)
    ## This supports the gnuplot backend.
    term = gnuplot_default_term ();
    opts_str = "";
  else
    ## Get the one word terminal id and save the remaining as options to
    ## be passed on to gnuplot.  The terminal may respect the backend.
    [term, opts_str] = gnuplot_trim_term (term);
    if (strcmpi (term, "pdf") && strcmpi (opts_str, "color"))
      ## FIXME -- "color" for the pdf terminal produces a gnuplot error.
      opts_str = "";
    endif
  endif

  implicit_margin = gnuplot_implicit_margin (term, opts_str);

  enhanced = gnuplot_is_enhanced_term (term);

  ## Set the terminal.
  if (! isempty (term))

    if (enhanced)
      enh_str = "enhanced";
    else
      enh_str = "";
    endif

    if (! isempty (h) && isfigure (h))

      ## Generate gnuplot title string for backend plot windows.
      if (output_to_screen (term) && ~strcmp (term, "dumb"))
        fig.numbertitle = get (h, "numbertitle");
        fig.name = get (h, "name");
        if (strcmpi (get (h, "numbertitle"), "on"))
          title_str = sprintf ("Figure %d", h);
        else
          title_str = "";
        endif
        if (! isempty (fig.name) && ! isempty (title_str))
          title_str = sprintf ("%s: %s", title_str, fig.name);
        elseif (! isempty (fig.name) && isempty (title_str))
          title_str = fig.name;
        endif
        if (! isempty (title_str))
          title_str = sprintf ("title \"%s\"", title_str);
        endif
      else
        title_str = "";
      endif
      if (! (any (strfind (opts_str, " size ") > 0) 
	  || any (strfind (opts_str, "size ") == 1)))
        ## Convert position to units used by gnuplot.
        if (output_to_screen (term))
          ## Get figure size in pixels.  Rely on listener
	  ## to handle coversion of position property.
	  units = get (h, "units");
	  unwind_protect
	    set (h, "units", "pixels");
	    position_in_pixesl = get (h, "position");
	  unwind_protect_cleanup
	    set (h, "units", units);
	  end_unwind_protect
	  gnuplot_pos = position_in_pixesl(1:2);
	  gnuplot_size = position_in_pixesl(3:4);
        else
          ## Get size of the printed plot in inches. Rely on listener
	  ## to handle coversion of papersize property.
	  paperunits = get (h, "paperunits");
	  unwind_protect
	    set (h, "paperunits", "inches");
            gnuplot_size = get (h, "papersize");
	  unwind_protect_cleanup
	    set (h, "paperunits", paperunits);
	  end_unwind_protect
          if (term_units_are_pixels (term))
	    ## Convert to inches using the property set by print().
	    gnuplot_size = gnuplot_size * get (h, "__pixels_per_inch__");
	  else
	    ## Implicit margins are in units of "inches"
	    gnuplot_size = gnuplot_size - implicit_margin;
          endif
        endif
	[begin_match, end_match, te, match] = regexp (opts_str, "(\\s-r\\d+)|(^-r\\d+)");
	if (! isempty (begin_match))
	  error ("gnuplot_drawnow.m: specifying resultion, '%s', not supported for terminal '%s'",
	         strtrim (match{1}), term)
	endif
        if (all (gnuplot_size > 0))
          ## Set terminal size.
          terminals_with_size = {"emf", "gif", "jpeg", "latex", "pbm", ...
                                 "pdf", "png", "postscript", "svg", ...
                                 "epslatex", "pstex", "pslatex"};
          if (__gnuplot_has_feature__ ("x11_figure_position"))
            terminals_with_size{end+1} = "x11";
          endif
          if (__gnuplot_has_feature__ ("wxt_figure_size"))
            terminals_with_size{end+1} = "wxt";
          endif
          if (any (strncmpi (term, terminals_with_size, 3)))
	    if (term_units_are_pixels (term))
              size_str = sprintf ("size %d,%d", gnuplot_size);
	    else
              size_str = sprintf ("size %.15g,%.15g", gnuplot_size);
	    endif
            if (strncmpi (term, "X11", 3) && __gnuplot_has_feature__ ("x11_figure_position"))
	      ## X11 allows the window to be positioned as well.
	      units = get (0, "units");
	      unwind_protect
	        set (0, "units", "pixels");
	        screen_size = get (0, "screensize")(3:4);
	      unwind_protect_cleanup
	        set (0, "units", units);
	      end_unwind_protect
              if (all (screen_size > 0))
                ## For X11, set the figure positon as well as the size
                ## gnuplot position is UL, Octave's is LL (same for screen/window)
                gnuplot_pos(2) = screen_size(2) - gnuplot_pos(2) - gnuplot_size(2);
                gnuplot_pos = max (gnuplot_pos, 1);
                size_str = sprintf ("%s position %d,%d", size_str, 
                                    gnuplot_pos(1), gnuplot_pos(2));
              endif
            endif
          elseif (strncmpi (term, "aqua", 3))
            ## Aqua has size, but the format is different.
            size_str = sprintf ("size %d %d", gnuplot_size);
          elseif (strncmpi (term, "dumb", 3))
            new_stream = 1;
            if (~isempty (getenv ("COLUMNS")) && ~isempty (getenv ("LINES")))
              ## Let dumb use full text screen size.
              size_str = ["size ", getenv("COLUMNS"), " ", getenv("LINES")];
            else
	      ## Use the gnuplot default.
              size_str = "";
            end
          elseif (strncmpi (term, "fig", 3))
            ## Fig also has size, but the format is different.
            size_str = sprintf ("size %.15g %.15g", gnuplot_size);
          elseif (any (strncmpi (term, {"corel", "hpgl"}, 3)))
            ## The size for corel and hpgl are goes at the end (implicit).
            size_str = sprintf ("%.15g %.15g", gnuplot_size);
          elseif (any (strncmpi (term, {"dxf"}, 3)))
            ## DXF uses autocad units.
            size_str = "";
          else
            size_str = "";
          endif
        else
          size_str = "";
	  warning ("gnuplot_set_term: size is zero")
        endif
      else
        ## A specified size take priority over the figure properies.
        size_str = "";
      endif
    else
      if isempty (h)
        disp ("gnuplot_set_term: figure handle is empty")
      elseif !isfigure(h)
        disp ("gnuplot_set_term: not a figure handle")
      endif
      title_str = "";
      size_str = "";
    endif

    ## Set the gnuplot terminal (type, enhanced, title, options & size).
    term_str = sprintf ("set terminal %s", term);
    if (! isempty (enh_str))
      term_str = sprintf ("%s %s", term_str, enh_str);
    endif
    if (! isempty (title_str))
      term_str = sprintf ("%s %s", term_str, title_str);
    endif
    if (nargin > 3 && ischar (opts_str))
      ## Options must go last.
      term_str = sprintf ("%s %s", term_str, opts_str);
    endif
    if (! isempty (size_str) && new_stream)
      ## size_str comes after other options to permit specification of
      ## the canvas size for terminals cdr/corel.
      term_str = sprintf ("%s %s", term_str, size_str);
    endif
    ## Work around the gnuplot feature of growing the x11 window and
    ## flickering window (x11, windows, & wxt) when the mouse and
    ## multiplot are set in gnuplot.
    fputs (plot_stream, "unset multiplot;\n");
    flickering_terms = {"x11", "windows", "wxt", "dumb"};
    if (! any (strcmp (term, flickering_terms))
        || numel (findall (h, "type", "axes")) > 1
        || numel (findall (h, "type", "image")) > 0)
      fprintf (plot_stream, "%s\n", term_str);
      if (nargin == 5)
        if (! isempty (file))
          fprintf (plot_stream, "set output '%s';\n", file);
        endif
      endif
      fputs (plot_stream, "set multiplot;\n");
    elseif (any (strcmp (term, flickering_terms)))
      fprintf (plot_stream, "%s\n", term_str);
      if (nargin == 5)
        if (! isempty (file))
          fprintf (plot_stream, "set output '%s';\n", file);
        endif
      endif
    endif
  else
    ## gnuplot will pick up the GNUTERM environment variable itself
    ## so no need to set the terminal type if not also setting the
    ## figure title, enhanced mode, or position.
  endif

endfunction

function term = gnuplot_default_term ()
  term = getenv ("GNUTERM");
  ## If not specified, guess the terminal type.
  if (isempty (term))
    if (ismac ())
      term = "aqua";
    elseif (! isunix ())
      term = "windows";
    elseif (! isempty (getenv ("DISPLAY")))
      term = "x11";
    else
      term = "dumb";
    endif
  endif
endfunction

function [term, opts] = gnuplot_trim_term (string)
  ## Extract the terminal type and terminal options (from print.m)
  string = deblank (string);
  n = strfind (string, ' ');
  if (isempty (n))
    term = string;
    opts = "";
  else
    term = string(1:(n-1));
    opts = string((n+1):end);
  endif
endfunction

function have_enhanced = gnuplot_is_enhanced_term (term)
  persistent enhanced_terminals;
  if (isempty (enhanced_terminals))
    ## Don't include pstex, pslatex or epslatex here as the TeX commands
    ## should not be interpreted in that case.
    enhanced_terminals = {"aqua", "dumb", "png", "jpeg", "gif", "pm", ...
                          "windows", "wxt", "svg", "postscript", "x11", "pdf"};
  endif
  if (nargin < 1)
    ## Determine the default gnuplot terminal.
    term = gnuplot_default_term ();
  endif
  have_enhanced = any (strncmp (enhanced_terminals, term, min (numel (term), 3)));
endfunction

function ret = output_to_screen (term)
  ret = any (strcmpi ({"aqua", "dumb", "wxt", "x11", "windows", "pm"}, term));
endfunction

function ret = term_units_are_pixels (term)
  ret = any (strncmpi ({"emf", "gif", "jpeg", "pbm", "png", "svg"}, term, 3));
endfunction

