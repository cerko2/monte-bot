// DO NOT EDIT!  Generated automatically by genprops.awk.

/*

Copyright (C) 2007, 2008, 2009 John W. Eaton

This file is part of Octave.

Octave is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 3 of the License, or (at your
option) any later version.

Octave is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with Octave; see the file COPYING.  If not, see
<http://www.gnu.org/licenses/>.

*/

#if !defined (graphics_h)
#define graphics_h 1

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <cctype>

#include <algorithm>
#include <list>
#include <map>
#include <set>
#include <string>

#include "lo-ieee.h"

#include "gripes.h"
#include "oct-map.h"
#include "oct-mutex.h"
#include "ov.h"

// FIXME -- maybe this should be a configure option?
// Matlab defaults to "Helvetica", but that causes problems for many
// gnuplot users.
#if !defined (OCTAVE_DEFAULT_FONTNAME)
#define OCTAVE_DEFAULT_FONTNAME "*"
#endif

class caseless_str : public std::string
{
public:
  typedef std::string::iterator iterator;
  typedef std::string::const_iterator const_iterator;

  caseless_str (void) : std::string () { }
  caseless_str (const std::string& s) : std::string (s) { }
  caseless_str (const char *s) : std::string (s) { }

  caseless_str (const caseless_str& name) : std::string (name) { }

  caseless_str& operator = (const caseless_str& pname)
  {
    std::string::operator = (pname);
    return *this;
  }

  operator std::string (void) const { return *this; }

  // Case-insensitive comparison.
  bool compare (const std::string& s, size_t limit = std::string::npos) const
  {
    const_iterator p1 = begin ();
    const_iterator p2 = s.begin ();

    size_t k = 0;

    while (p1 != end () && p2 != s.end () && k++ < limit)
      {
	if (std::tolower (*p1) != std::tolower (*p2))
	  return false;

	*p1++;
	*p2++;
      }

    return (limit == std::string::npos) ? size () == s.size () : k == limit;
  }
};

// ---------------------------------------------------------------------

class graphics_handle
{
public:
  graphics_handle (void) : val (octave_NaN) { }

  graphics_handle (const octave_value& a);

  graphics_handle (int a) : val (a) { }

  graphics_handle (double a) : val (a) { }

  graphics_handle (const graphics_handle& a) : val (a.val) { }

  graphics_handle& operator = (const graphics_handle& a)
  {
    if (&a != this)
      val = a.val;

    return *this;
  }

  ~graphics_handle (void) { }

  double value (void) const { return val; }

  octave_value as_octave_value (void) const
  {
    return ok () ? octave_value (val) : octave_value (Matrix ());
  }

  graphics_handle operator ++ (void)
  {
    ++val;
    return *this;
  }

  graphics_handle operator ++ (int)
  {
    graphics_handle h = *this;
    ++val;
    return h;
  }

  graphics_handle operator -- (void)
  {
    --val;
    return *this;
  }

  graphics_handle operator -- (int)
  {
    graphics_handle h = *this;
    --val;
    return h;
  }

  bool ok (void) const { return ! xisnan (val); }

private:
  double val;
};

inline bool
operator == (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () == b.value ();
}

inline bool
operator != (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () != b.value ();
}

inline bool
operator < (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () < b.value ();
}

inline bool
operator <= (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () <= b.value ();
}

inline bool
operator >= (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () >= b.value ();
}

inline bool
operator > (const graphics_handle& a, const graphics_handle& b)
{
  return a.value () > b.value ();
}

// ---------------------------------------------------------------------

class base_scaler
{
public:
  base_scaler (void) { }

  virtual ~base_scaler (void) { }

  virtual Matrix scale (const Matrix& m) const
    {
      error ("invalid axis scale");
      return m;
    }

  virtual NDArray scale (const NDArray& m) const
    {
      error ("invalid axis scale");
      return m;
    }

  virtual double scale (double d) const
    {
      error ("invalid axis scale");
      return d;
    }
  
  virtual double unscale (double d) const
    {
      error ("invalid axis scale");
      return d;
    }

  virtual base_scaler* clone () const
    { return new base_scaler (); }

  virtual bool is_linear (void) const
    { return false; }
};

class lin_scaler : public base_scaler
{
public:
  lin_scaler (void) { }

  Matrix scale (const Matrix& m) const { return m; }

  NDArray scale (const NDArray& m) const { return m; }

  double scale (double d) const { return d; }

  double unscale (double d) const { return d; }

  base_scaler* clone (void) const { return new lin_scaler (); }

  bool is_linear (void) const { return true; }
};

class log_scaler : public base_scaler
{
public:
  log_scaler (void) { }

  Matrix scale (const Matrix& m) const
    {
      Matrix retval (m.rows (), m.cols ());

      do_scale (m.data (), retval.fortran_vec (), m.numel ());
      return retval;
    }

  NDArray scale (const NDArray& m) const
    {
      NDArray retval (m.dims ());

      do_scale (m.data (), retval.fortran_vec (), m.numel ());
      return retval;
    }

  double scale (double d) const
    { return log10 (d); }

  double unscale (double d) const
    { return pow (10.0, d); }

  base_scaler* clone (void) const
    { return new log_scaler (); }

private:
  void do_scale (const double *src, double *dest, int n) const
    {
      for (int i = 0; i < n; i++)
	dest[i] = log10(src[i]);
    }
};

class scaler
{
public:
  scaler (void) : rep (new base_scaler ()) { }

  scaler (const scaler& s) : rep (s.rep->clone()) { }

  ~scaler (void) { delete rep; }

  Matrix scale (const Matrix& m) const
    { return rep->scale (m); }

  NDArray scale (const NDArray& m) const
    { return rep->scale (m); }

  double scale (double d) const
    { return rep->scale (d); }

  double unscale (double d) const
    { return rep->unscale (d); }

  bool is_linear (void) const
    { return rep->is_linear (); }

  scaler& operator = (const scaler& s)
    {
      if (rep)
	{
	  delete rep;
	  rep = 0;
	}

      rep = s.rep->clone ();

      return *this;
    }

  scaler& operator = (const std::string& s)
    {
      if (rep)
	{
	  delete rep;
	  rep = 0;
	}

      if (s == "log")
	rep = new log_scaler ();
      else if (s == "linear")
	rep = new lin_scaler ();
      else
	rep = new base_scaler ();

      return *this;
    }

private:
  base_scaler *rep;
};

// ---------------------------------------------------------------------

class property;

enum listener_mode { POSTSET };

class base_property
{
public:
  friend class property;

public:
  base_property (void) : id (-1), count (1) { }

  base_property (const std::string& s, const graphics_handle& h)
    : id (-1), count (1), name (s), parent (h), hidden (false) { }

  base_property (const base_property& p)
    : id (-1), count (1), name (p.name), parent (p.parent), hidden (p.hidden) { }

  virtual ~base_property (void) { }

  bool ok (void) const { return parent.ok (); }

  std::string get_name (void) const { return name; }

  void set_name (const std::string& s) { name = s; }

  graphics_handle get_parent (void) const { return parent; }

  void set_parent (const graphics_handle &h) { parent = h; }

  bool is_hidden (void) const { return hidden; }

  void set_hidden (bool flag) { hidden = flag; }

  int get_id (void) const { return id; }

  void set_id (int d) { id = d; }

  // Sets property value, notifies backend.
  // If do_run is true, runs associated listeners.
  bool set (const octave_value& v, bool do_run = true);
  
  virtual octave_value get (void) const
    {
      error ("get: invalid property \"%s\"", name.c_str ());
      return octave_value ();
    }

  base_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  void add_listener (const octave_value& v, listener_mode mode = POSTSET)
    {
      octave_value_list& l = listeners[mode];
      l.resize (l.length () + 1, v);
    }

  void delete_listener (const octave_value& v = octave_value (), 
			listener_mode mode = POSTSET)
    {
      octave_value_list& l = listeners[mode];

      if (v.is_defined ())
	{
	  bool found = false;
	  int i;

	  for (i = 0; i < l.length (); i++)
	    {
	      if (v.internal_rep () == l(i).internal_rep ())
		{
		  found = true;
		  break;
		}
	    }
	  if (found)
	    {
	      for (int j = i; j < l.length() - 1; j++)
		l(j) = l (j + 1);

	      l.resize (l.length () - 1);
	    }
	}
      else
	l.resize (0);

    }

  OCTINTERP_API void run_listeners (listener_mode mode = POSTSET);

  virtual base_property* clone (void) const
    { return new base_property (*this); }

protected:
  virtual bool do_set (const octave_value&)
    {
      error ("set: invalid property \"%s\"", name.c_str ());
      return false;
    }

private:
  typedef std::map<listener_mode, octave_value_list> listener_map;
  typedef std::map<listener_mode, octave_value_list>::iterator listener_map_iterator;
  typedef std::map<listener_mode, octave_value_list>::const_iterator listener_map_const_iterator;

private:
  int id;
  int count;
  std::string name;
  graphics_handle parent;
  bool hidden;
  listener_map listeners;
};

// ---------------------------------------------------------------------

class string_property : public base_property
{
public:
  string_property (const std::string& s, const graphics_handle& h,
                   const std::string& val = "")
    : base_property (s, h), str (val) { }

  string_property (const string_property& p)
    : base_property (p), str (p.str) { }

  octave_value get (void) const
    { return octave_value (str); }

  std::string string_value (void) const { return str; }

  string_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new string_property (*this); }

protected:
  bool do_set (const octave_value& val)
    {
      if (val.is_string ())
	{
	  std::string new_str = val.string_value ();

	  if (new_str != str)
	    {
	      str = new_str;
	      return true;
	    }
	}
      else
        error ("set: invalid string property value for \"%s\"",
               get_name ().c_str ());
      return false;
    }

private:
  std::string str;
};

// ---------------------------------------------------------------------

class radio_values
{
public:
  OCTINTERP_API radio_values (const std::string& opt_string = std::string ());

  radio_values (const radio_values& a)
    : default_val (a.default_val), possible_vals (a.possible_vals) { }

  radio_values& operator = (const radio_values& a)
  {
    if (&a != this)
      {
	default_val = a.default_val;
	possible_vals = a.possible_vals;
      }

    return *this;
  }

  std::string default_value (void) const { return default_val; }

  bool validate (const std::string& val)
  {
    bool retval = true;

    if (! contains (val))
      {
	error ("invalid value = %s", val.c_str ());
	retval = false;
      }

    return retval;
  }
  
  bool contains (const std::string& val)
  {
    return (possible_vals.find (val) != possible_vals.end ());
  }

private:
  // Might also want to cache
  std::string default_val;
  std::set<caseless_str> possible_vals;
};

class radio_property : public base_property
{
public:
  radio_property (const std::string& nm, const graphics_handle& h,
                  const radio_values& v = radio_values ())
    : base_property (nm, h),
      vals (v), current_val (v.default_value ()) { }

  radio_property (const std::string& nm, const graphics_handle& h,
                  const std::string& v)
    : base_property (nm, h),
      vals (v), current_val (vals.default_value ()) { }

  radio_property (const std::string& nm, const graphics_handle& h,
                  const radio_values& v, const std::string& def)
    : base_property (nm, h),
      vals (v), current_val (def) { }

  radio_property (const radio_property& p)
    : base_property (p), vals (p.vals), current_val (p.current_val) { }

  octave_value get (void) const { return octave_value (current_val); }

  const std::string& current_value (void) const { return current_val; }

  bool is (const caseless_str& v) const
    { return v.compare (current_val); }

  radio_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new radio_property (*this); }

protected:
  bool do_set (const octave_value& newval) 
  {
    if (newval.is_string ())
      {
        std::string s = newval.string_value ();
        if (vals.validate (s))
	  {
	    if (s != current_val)
	      {
		current_val = s;
		return true;
	      }
	  }
        else
          error ("set: invalid value for radio property \"%s\" (value = %s)",
              get_name ().c_str (), s.c_str ());
      }
    else	
      error ("set: invalid value for radio property \"%s\"",
          get_name ().c_str ());
    return false;
  }

private:
  radio_values vals;
  std::string current_val;
};

// ---------------------------------------------------------------------

class color_values
{
public:
  color_values (double r = 0, double g = 0, double b = 1)
    : xrgb (1, 3)
  {
    xrgb(0) = r;
    xrgb(1) = g;
    xrgb(2) = b;

    validate ();
  }

  color_values (std::string str)
    : xrgb (1, 3)
  {
    if (! str2rgb (str))
      error ("invalid color specification: %s", str.c_str ());
  }

  color_values (const color_values& c)
    : xrgb (c.xrgb)
  { }

  color_values& operator = (const color_values& c)
  {
    if (&c != this)
      xrgb = c.xrgb;

    return *this;
  }

  bool operator == (const color_values& c) const
    {
      return (xrgb(0) == c.xrgb(0)
	      && xrgb(1) == c.xrgb(1)
	      && xrgb(2) == c.xrgb(2));
    }

  bool operator != (const color_values& c) const
    { return ! (*this == c); }

  Matrix rgb (void) const { return xrgb; }

  operator octave_value (void) const { return xrgb; }

  void validate (void) const
  {
    for (int i = 0; i < 3; i++)
      {
	if (xrgb(i) < 0 ||  xrgb(i) > 1)
	  {
	    error ("invalid RGB color specification");
	    break;
	  }
      }
  }

private:
  Matrix xrgb;

  OCTINTERP_API bool str2rgb (std::string str);
};

class color_property : public base_property
{
public:
  color_property (const color_values& c, const radio_values& v)
    : base_property ("", graphics_handle ()),
      current_type (color_t), color_val (c), radio_val (v),
      current_val (v.default_value ())
  { }

  color_property (const std::string& nm, const graphics_handle& h,
                  const color_values& c = color_values (),
                  const radio_values& v = radio_values ())
    : base_property (nm, h),
      current_type (color_t), color_val (c), radio_val (v),
      current_val (v.default_value ())
  { }

  color_property (const std::string& nm, const graphics_handle& h,
                  const radio_values& v)
    : base_property (nm, h),
      current_type (radio_t), color_val (color_values ()), radio_val (v),
      current_val (v.default_value ())
  { }

  color_property (const std::string& nm, const graphics_handle& h,
                  const std::string& v)
    : base_property (nm, h),
      current_type (radio_t), color_val (color_values ()), radio_val (v),
      current_val (radio_val.default_value ())
  { }
  
  color_property (const std::string& nm, const graphics_handle& h,
                  const color_property& v)
    : base_property (nm, h),
      current_type (v.current_type), color_val (v.color_val),
      radio_val (v.radio_val), current_val (v.current_val)
  { }

  color_property (const color_property& p)
    : base_property (p), current_type (p.current_type),
      color_val (p.color_val), radio_val (p.radio_val),
      current_val (p.current_val) { }

  octave_value get (void) const
  {
    if (current_type == color_t)
      return color_val.rgb ();

    return current_val;
  }

  bool is_rgb (void) const { return (current_type == color_t); }

  bool is_radio (void) const { return (current_type == radio_t); }

  bool is (const std::string& v) const
    { return (is_radio () && current_val == v); }

  Matrix rgb (void) const
  {
    if (current_type != color_t)
      error ("color has no rgb value");

    return color_val.rgb ();
  }

  const std::string& current_value (void) const
  {
    if (current_type != radio_t)
      error ("color has no radio value");

    return current_val;
  }

  color_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  operator octave_value (void) const { return get (); }

  base_property* clone (void) const { return new color_property (*this); }

protected:
  OCTINTERP_API bool do_set (const octave_value& newval);

private:
  enum current_enum { color_t, radio_t } current_type;
  color_values color_val;
  radio_values radio_val;
  std::string current_val;
};

// ---------------------------------------------------------------------

class double_property : public base_property
{
public:
  double_property (const std::string& nm, const graphics_handle& h,
                   double d = 0)
    : base_property (nm, h),
      current_val (d) { }

  double_property (const double_property& p)
    : base_property (p), current_val (p.current_val) { }

  octave_value get (void) const { return octave_value (current_val); }

  double double_value (void) const { return current_val; }

  double_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new double_property (*this); }

protected:
  bool do_set (const octave_value& v)
    {
      if (v.is_scalar_type () && v.is_real_type ())
	{
	  double new_val = v.double_value ();

	  if (new_val != current_val)
	    {
	      current_val = new_val;
	      return true;
	    }
	}
      else
        error ("set: invalid value for double property \"%s\"",
               get_name ().c_str ());
      return false;
    }

private:
  double current_val;
};

// ---------------------------------------------------------------------

class double_radio_property : public base_property
{
public:
  double_radio_property (double d, const radio_values& v)
      : base_property ("", graphics_handle ()),
        current_type (double_t), dval (d), radio_val (v),
	current_val (v.default_value ())
  { }

  double_radio_property (const std::string& nm, const graphics_handle& h,
			 const std::string& v)
      : base_property (nm, h),
        current_type (radio_t), dval (0), radio_val (v),
	current_val (radio_val.default_value ())
  { }

  double_radio_property (const std::string& nm, const graphics_handle& h,
			 const double_radio_property& v)
      : base_property (nm, h),
        current_type (v.current_type), dval (v.dval),
	radio_val (v.radio_val), current_val (v.current_val)
  { }

  double_radio_property (const double_radio_property& p)
    : base_property (p), current_type (p.current_type),
      dval (p.dval), radio_val (p.radio_val),
      current_val (p.current_val) { }

  octave_value get (void) const
  {
    if (current_type == double_t)
      return dval;

    return current_val;
  }

  bool is_double (void) const { return (current_type == double_t); }

  bool is_radio (void) const { return (current_type == radio_t); }

  bool is (const std::string& v) const
    { return (is_radio () && current_val == v); }

  double double_value (void) const
  {
    if (current_type != double_t)
      error ("%s: property has no double", get_name ().c_str ());

    return dval;
  }

  const std::string& current_value (void) const
  {
    if (current_type != radio_t)
      error ("%s: property has no radio value");

    return current_val;
  }

  double_radio_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  operator octave_value (void) const { return get (); }

  base_property* clone (void) const
    { return new double_radio_property (*this); }

protected:
  OCTINTERP_API bool do_set (const octave_value& v);

private:
  enum current_enum { double_t, radio_t } current_type;
  double dval;
  radio_values radio_val;
  std::string current_val;
};

// ---------------------------------------------------------------------

class array_property : public base_property
{
public:
  array_property (void)
      : base_property ("", graphics_handle ()), data (Matrix ())
    {
      get_data_limits ();
    }

  array_property (const std::string& nm, const graphics_handle& h,
                  const octave_value& m)
      : base_property (nm, h), data (m)
    {
      get_data_limits ();
    }

  // This copy constructor is only intended to be used
  // internally to access min/max values; no need to
  // copy constraints.
  array_property (const array_property& p)
    : base_property (p), data (p.data),
      xmin (p.xmin), xmax (p.xmax), xminp (p.xminp) { }

  octave_value get (void) const { return data; }

  void add_constraint (const std::string& type)
    { type_constraints.push_back (type); }

  void add_constraint (const dim_vector& dims)
    { size_constraints.push_back (dims); }

  double min_val (void) const { return xmin; }
  double max_val (void) const { return xmax; }
  double min_pos (void) const { return xminp; }

  Matrix get_limits (void) const
    {
      Matrix m (1, 3);
      
      m(0) = min_val ();
      m(1) = max_val ();
      m(2) = min_pos ();

      return m;
    }

  array_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const
    {
      array_property *p = new array_property (*this);

      p->type_constraints = type_constraints;
      p->size_constraints = size_constraints;
      
      return p;
    }

protected:
  bool do_set (const octave_value& v)
    {
      if (validate (v))
	{
	  // FIXME -- should we check for actual data change?
	  if (! is_equal (v))
	    {
	      data = v;

	      get_data_limits ();

	      return true;
	    }
	}
      else
        error ("invalid value for array property \"%s\"",
               get_name ().c_str ());
      
      return false;
    }

private:
  OCTINTERP_API bool validate (const octave_value& v);

  OCTINTERP_API bool is_equal (const octave_value& v) const;

  OCTINTERP_API void get_data_limits (void);

protected:
  octave_value data;
  double xmin;
  double xmax;
  double xminp;
  std::list<std::string> type_constraints;
  std::list<dim_vector> size_constraints;
};

class row_vector_property : public array_property
{
public:
  row_vector_property (const std::string& nm, const graphics_handle& h,
		       const octave_value& m)
    : array_property (nm, h, m)
  {
    add_constraint (dim_vector (-1, 1));
    add_constraint (dim_vector (1, -1));
  }

  row_vector_property (const row_vector_property& p)
    : array_property (p)
  {
    add_constraint (dim_vector (-1, 1));
    add_constraint (dim_vector (1, -1));
  }

  void add_constraint (const std::string& type)
  {
    array_property::add_constraint (type);
  }

  void add_constraint (const dim_vector& dims)
  {
    array_property::add_constraint (dims);
  }

  void add_constraint (octave_idx_type len)
  {
    size_constraints.remove (dim_vector (1, -1));
    size_constraints.remove (dim_vector (-1, 1));

    add_constraint (dim_vector (1, len));
    add_constraint (dim_vector (len, 1));
  }

  row_vector_property& operator = (const octave_value& val)
  {
    set (val);
    return *this;
  }

  base_property* clone (void) const
    {
      row_vector_property *p = new row_vector_property (*this);

      p->type_constraints = type_constraints;
      p->size_constraints = size_constraints;

      return p;
    }

protected:
  bool do_set (const octave_value& v)
  {
    bool retval = array_property::do_set (v);

    if (! error_state)
      {
	dim_vector dv = data.dims ();

	if (dv(0) > 1 && dv(1) == 1)
	  {
	    int tmp = dv(0);
	    dv(0) = dv(1);
	    dv(1) = tmp;

	    data = data.reshape (dv);
	  }

	return retval;
      }

    return false;
  }

private:
  OCTINTERP_API bool validate (const octave_value& v);
};

// ---------------------------------------------------------------------

class bool_property : public radio_property
{
public:
  bool_property (const std::string& nm, const graphics_handle& h,
                 bool val)
    : radio_property (nm, h, radio_values (val ? "{on}|off" : "on|{off}"))
    { }

  bool_property (const std::string& nm, const graphics_handle& h,
                 const char* val)
    : radio_property (nm, h, radio_values ("on|off"), val)
    { }

  bool_property (const bool_property& p)
    : radio_property (p) { }

  bool is_on (void) const { return is ("on"); }
  
  bool_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new bool_property (*this); }

protected:
  bool do_set (const octave_value& val)
    {
      if (val.is_bool_scalar ())
        return radio_property::do_set (val.bool_value () ? "on" : "off");
      else
        return radio_property::do_set (val);
    }
};

// ---------------------------------------------------------------------

class handle_property : public base_property
{
public:
  handle_property (const std::string& nm, const graphics_handle& h,
                   const graphics_handle& val = graphics_handle ())
    : base_property (nm, h),
      current_val (val) { }

  handle_property (const handle_property& p)
    : base_property (p), current_val (p.current_val) { }

  octave_value get (void) const { return current_val.as_octave_value (); }

  graphics_handle handle_value (void) const { return current_val; }

  handle_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  handle_property& operator = (const graphics_handle& h)
    {
      set (octave_value (h.value ()));
      return *this;
    }

  base_property* clone (void) const { return new handle_property (*this); }

protected:
  OCTINTERP_API bool do_set (const octave_value& v);

private:
  graphics_handle current_val;
};

// ---------------------------------------------------------------------

class any_property : public base_property
{
public:
  any_property (const std::string& nm, const graphics_handle& h,
                  const octave_value& m = Matrix ())
    : base_property (nm, h), data (m) { }

  any_property (const any_property& p)
    : base_property (p), data (p.data) { }

  octave_value get (void) const { return data; }

  any_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new any_property (*this); }

protected:
  bool do_set (const octave_value& v)
    {
      data = v;
      return true;
    }

private:
  octave_value data;
};

// ---------------------------------------------------------------------

class callback_property : public base_property
{
public:
  callback_property (const std::string& nm, const graphics_handle& h,
                     const octave_value& m)
    : base_property (nm, h), callback (m) { }

  callback_property (const callback_property& p)
    : base_property (p), callback (p.callback) { }

  octave_value get (void) const { return callback; }

  OCTINTERP_API void execute (const octave_value& data = octave_value ()) const;

  callback_property& operator = (const octave_value& val)
    {
      set (val);
      return *this;
    }

  base_property* clone (void) const { return new callback_property (*this); }

protected:
  bool do_set (const octave_value& v)
    {
      if (validate (v))
	{
	  callback = v;
	  return true;
	}
      else
        error ("invalid value for callback property \"%s\"",
               get_name ().c_str ());
      return false;
    }

private:
  OCTINTERP_API bool validate (const octave_value& v) const;

private:
  octave_value callback;
};

// ---------------------------------------------------------------------

class property
{
public:
  property (void) : rep (new base_property ("", graphics_handle ()))
    { }

  property (base_property *bp, bool persist = false) : rep (bp)
    { if (persist) rep->count++; }

  property (const property& p)
    {
      rep = p.rep;
      rep->count++;
    }

  ~property (void)
    {
      if (--rep->count <= 0)
        delete rep;
    }

  bool ok (void) const
    { return rep->ok (); }

  std::string get_name (void) const
    { return rep->get_name (); }

  void set_name (const std::string& name)
    { rep->set_name (name); }

  graphics_handle get_parent (void) const
    { return rep->get_parent (); }

  void set_parent (const graphics_handle& h)
    { rep->set_parent (h); }

  bool is_hidden (void) const
    { return rep->is_hidden (); }

  void set_hidden (bool flag)
    { rep->set_hidden (flag); }

  int get_id (void) const
    { return rep->get_id (); }

  void set_id (int d)
    { rep->set_id (d); }

  octave_value get (void) const
    { return rep->get (); }

  bool set (const octave_value& val)
    { return rep->set (val); }

  property& operator = (const octave_value& val)
    {
      *rep = val;
      return *this;
    }

  property& operator = (const property& p)
    {
      if (rep && --rep->count <= 0)
        delete rep;
      
      rep = p.rep;
      rep->count++;

      return *this;
    }

  void add_listener (const octave_value& v, listener_mode mode = POSTSET)
    { rep->add_listener (v, mode); }

  void delete_listener (const octave_value& v = octave_value (), 
			listener_mode mode = POSTSET)
  { rep->delete_listener (v, mode); }

  void run_listeners (listener_mode mode = POSTSET)
    { rep->run_listeners (mode); }

  OCTINTERP_API static
      property create (const std::string& name, const graphics_handle& parent,
		       const caseless_str& type,
		       const octave_value_list& args);

  property clone (void) const
    { return property (rep->clone ()); }

  /*
  const string_property& as_string_property (void) const
    { return *(dynamic_cast<string_property*> (rep)); }

  const radio_property& as_radio_property (void) const
    { return *(dynamic_cast<radio_property*> (rep)); }

  const color_property& as_color_property (void) const
    { return *(dynamic_cast<color_property*> (rep)); }

  const double_property& as_double_property (void) const
    { return *(dynamic_cast<double_property*> (rep)); }

  const bool_property& as_bool_property (void) const
    { return *(dynamic_cast<bool_property*> (rep)); }
  
  const handle_property& as_handle_property (void) const
    { return *(dynamic_cast<handle_property*> (rep)); }
    */

private:
  base_property *rep;
};

// ---------------------------------------------------------------------

class property_list
{
public:
  typedef std::map<std::string, octave_value> pval_map_type;
  typedef std::map<std::string, pval_map_type> plist_map_type;
  
  typedef pval_map_type::iterator pval_map_iterator;
  typedef pval_map_type::const_iterator pval_map_const_iterator;

  typedef plist_map_type::iterator plist_map_iterator;
  typedef plist_map_type::const_iterator plist_map_const_iterator;

  property_list (const plist_map_type& m = plist_map_type ())
    : plist_map (m) { }

  ~property_list (void) { }

  void set (const caseless_str& name, const octave_value& val);

  octave_value lookup (const caseless_str& name) const;

  plist_map_iterator begin (void) { return plist_map.begin (); }
  plist_map_const_iterator begin (void) const { return plist_map.begin (); }

  plist_map_iterator end (void) { return plist_map.end (); }
  plist_map_const_iterator end (void) const { return plist_map.end (); }

  plist_map_iterator find (const std::string& go_name)
  {
    return plist_map.find (go_name);
  }

  plist_map_const_iterator find (const std::string& go_name) const
  {
    return plist_map.find (go_name);
  }

  Octave_map as_struct (const std::string& prefix_arg) const;

private:
  plist_map_type plist_map;
};

// ---------------------------------------------------------------------

class graphics_backend;
class graphics_object;

class base_graphics_backend
{
public:
  friend class graphics_backend;

public:
  base_graphics_backend (const std::string& nm)
      : name (nm), count (0) { }

  virtual ~base_graphics_backend (void) { }

  std::string get_name (void) const { return name; }

  virtual bool is_valid (void) const { return false; }

  virtual void redraw_figure (const graphics_object&) const
    { gripe_invalid ("redraw_figure"); }

  virtual void print_figure (const graphics_object&, const std::string&,
			     const std::string&, bool,
			     const std::string& = "") const
    { gripe_invalid ("print_figure"); }

  virtual Matrix get_canvas_size (const graphics_handle&) const
    {
      gripe_invalid ("get_canvas_size");
      return Matrix (1, 2, 0.0);
    }

  virtual double get_screen_resolution (void) const
    {
      gripe_invalid ("get_screen_resolution");
      return 72.0;
    }
  
  virtual Matrix get_screen_size (void) const
    {
      gripe_invalid ("get_screen_size");
      return Matrix (1, 2, 0.0);
    }

  // Called when graphics object using this backend changes it's property.
  virtual void property_changed (const graphics_object&, int)
    { gripe_invalid ("property_changed"); }

  void property_changed (const graphics_handle&, int);
  
  // Called when new object using this backend is created.
  virtual void object_created (const graphics_object&)
    { gripe_invalid ("object_created"); }

  void object_created (const graphics_handle&);

  // Called when object using this backend is destroyed.
  virtual void object_destroyed (const graphics_object&)
    { gripe_invalid ("object_destroyed"); }

  void object_destroyed (const graphics_handle&);

private:
  std::string name;
  int count;

private:
  void gripe_invalid (const std::string& fname) const
    {
      if (! is_valid ())
	error ("%s: invalid graphics backend", fname.c_str ());
    }
};

class graphics_backend
{
public:
  graphics_backend (void)
      : rep (new base_graphics_backend ("unknown"))
    {
      rep->count++;
    }

  graphics_backend (base_graphics_backend* b)
      : rep (b)
    {
      rep->count++;
    }

  graphics_backend (const graphics_backend& b)
      : rep (b.rep)
    {
      rep->count++;
    }

  ~graphics_backend (void)
    {
      if (--rep->count == 0)
	delete rep;
    }

  graphics_backend& operator = (const graphics_backend& b)
    {
      if (rep != b.rep)
	{
	  if (--rep->count == 0)
	    delete rep;

	  rep = b.rep;
	  rep->count++;
	}

      return *this;
    }

  operator bool (void) const { return rep->is_valid (); }

  std::string get_name (void) const { return rep->get_name (); }

  void redraw_figure (const graphics_object& go) const
    { rep->redraw_figure (go); }
  
  void print_figure (const graphics_object& go, const std::string& term,
		     const std::string& file, bool mono,
		     const std::string& debug_file = "") const
    { rep->print_figure (go, term, file, mono, debug_file); }

  Matrix get_canvas_size (const graphics_handle& fh) const
    { return rep->get_canvas_size (fh); }

  double get_screen_resolution (void) const
    { return rep->get_screen_resolution (); }

  Matrix get_screen_size (void) const
    { return rep->get_screen_size (); }

  // Notifies backend that object't property has changed.
  void property_changed (const graphics_object& go, int id)
    { rep->property_changed (go, id); }
  
  void property_changed (const graphics_handle& h, int id)
    { rep->property_changed (h, id); }

  // Notifies backend that new object was created.
  void object_created (const graphics_object& go)
    { rep->object_created (go); }
  
  void object_created (const graphics_handle& h)
    { rep->object_created (h); }
  
  // Notifies backend that object was destroyed.
  // This is called only for explicitly deleted object. Children are
  // deleted implicitly and backend isn't notified.
  void object_destroyed (const graphics_object& go)
    { rep->object_destroyed (go); }
  
  void object_destroyed (const graphics_handle& h)
    { rep->object_destroyed (h); }
  
  OCTINTERP_API static graphics_backend default_backend (void);

  static void register_backend (const graphics_backend& b)
    { available_backends[b.get_name ()] = b; }

  static void unregister_backend (const std::string& name)
    { available_backends.erase (name); }

  static graphics_backend find_backend (const std::string& name)
  {
    const_available_backends_iterator p = available_backends.find (name);

    if (p != available_backends.end ())
      return p->second;
    else
      return default_backend ();
  }

  static Cell available_backends_list (void)
  {
    Cell m (1 , available_backends.size ());
    const_available_backends_iterator p;
    int i;
    
    for (i = 0,p = available_backends.begin (); p !=  available_backends.end (); p++,i++)
      m(i) = p->first;

    return m;
  }

private:
  base_graphics_backend *rep;

  static OCTINTERP_API std::map<std::string, graphics_backend> available_backends;

  typedef std::map<std::string, graphics_backend>::iterator available_backends_iterator;
  typedef std::map<std::string, graphics_backend>::const_iterator const_available_backends_iterator;
};

// ---------------------------------------------------------------------

class base_graphics_object;

class OCTINTERP_API base_properties
{
public:
  base_properties (const std::string& ty = "unknown",
                   const graphics_handle& mh = graphics_handle (),
                   const graphics_handle& p = graphics_handle ());

  virtual ~base_properties (void) { }

  virtual std::string graphics_object_name (void) const { return "unknonwn"; }

  void mark_modified (void);

  void override_defaults (base_graphics_object& obj);

  // Look through DEFAULTS for properties with given CLASS_NAME, and
  // apply them to the current object with set (virtual method).

  void set_from_list (base_graphics_object& obj, property_list& defaults);

  void insert_property (const std::string& name, property p)
    {
      p.set_name (name);
      p.set_parent (__myhandle__);
      all_props[name] = p;
    }

  virtual void set (const caseless_str&, const octave_value&)
  {
    panic_impossible ();
  }

  void set (const caseless_str& pname, const std::string& cname,
	    const octave_value& val);

  virtual octave_value get (const caseless_str& pname) const;

  virtual octave_value get (bool all = false) const;

  virtual property get_property (const caseless_str& pname);

  bool has_property (const caseless_str& pname);

  bool is_modified (void) const { return is___modified__ (); }
 
  virtual void remove_child (const graphics_handle& h);

  virtual void adopt (const graphics_handle& h)
  {
    octave_idx_type n = children.numel ();
    children.resize (n+1, 1);
    for (octave_idx_type i = n; i > 0; i--)
      children(i) = children(i-1);
    children(0) = h.value ();
    mark_modified ();
  }

  virtual graphics_backend get_backend (void) const;

  virtual Matrix get_boundingbox (bool /*internal*/ = false) const
    { return Matrix (1, 4, 0.0); }

  virtual void update_boundingbox (void);

  virtual void add_listener (const caseless_str&, const octave_value&,
			     listener_mode = POSTSET);

  virtual void delete_listener (const caseless_str&, const octave_value&,
				listener_mode = POSTSET);

  void set_tag (const octave_value& val) { tag = val; }

  void set_parent (const octave_value& val);

  Matrix get_all_children (void) const { return children; }

  void set_children (const octave_value& val);

  void set_modified (const octave_value& val) { set___modified__ (val); }

  void set___modified__ (const octave_value& val) { __modified__ = val; }

  void reparent (const graphics_handle& new_parent) { parent = new_parent; }

  // Update data limits for AXIS_TYPE (xdata, ydata, etc.) in the parent
  // axes object.

  virtual void update_axis_limits (const std::string& axis_type) const;

  virtual void delete_children (void);

  static property_list::pval_map_type factory_defaults (void);

  // FIXME -- these functions should be generated automatically by the
  // genprops.awk script.
  //
  // EMIT_BASE_PROPERTIES_GET_FUNCTIONS

  virtual octave_value get_xlim (void) const { return octave_value (); }
  virtual octave_value get_ylim (void) const { return octave_value (); }
  virtual octave_value get_zlim (void) const { return octave_value (); }
  virtual octave_value get_clim (void) const { return octave_value (); }
  virtual octave_value get_alim (void) const { return octave_value (); }

  virtual bool is_xliminclude (void) const { return false; }
  virtual bool is_yliminclude (void) const { return false; }
  virtual bool is_zliminclude (void) const { return false; }
  virtual bool is_climinclude (void) const { return false; }
  virtual bool is_aliminclude (void) const { return false; }

  bool is_handle_visible (void) const
  {
    return ! handlevisibility.is ("off");
  }

  static std::map<std::string, std::set<std::string> > all_dynamic_properties;
 
  static bool has_dynamic_property (const std::string& pname,
				    const std::string& cname);

protected:
  void set_dynamic (const caseless_str& pname, const std::string& cname,
		    const octave_value& val);

  octave_value get_dynamic (const caseless_str& pname) const;

  octave_value get_dynamic (bool all = false) const;

  property get_property_dynamic (const caseless_str& pname);

public:


  static bool has_property (const std::string& pname, const std::string& cname);

protected:

  bool_property beingdeleted;
  radio_property busyaction;
  callback_property buttondownfcn;
  Matrix children;
  bool_property clipping;
  callback_property createfcn;
  callback_property deletefcn;
  radio_property handlevisibility;
  bool_property hittest;
  bool_property interruptible;
  handle_property parent;
  bool_property selected;
  bool_property selectionhighlight;
  string_property tag;
  string_property type;
  any_property userdata;
  bool_property visible;
  bool_property __modified__;
  graphics_handle __myhandle__;
  handle_property uicontextmenu;

public:

  enum
  {
    BEINGDELETED = 0,
    BUSYACTION = 1,
    BUTTONDOWNFCN = 2,
    CHILDREN = 3,
    CLIPPING = 4,
    CREATEFCN = 5,
    DELETEFCN = 6,
    HANDLEVISIBILITY = 7,
    HITTEST = 8,
    INTERRUPTIBLE = 9,
    PARENT = 10,
    SELECTED = 11,
    SELECTIONHIGHLIGHT = 12,
    TAG = 13,
    TYPE = 14,
    USERDATA = 15,
    VISIBLE = 16,
    __MODIFIED__ = 17,
    __MYHANDLE__ = 18,
    UICONTEXTMENU = 19
  };

  bool is_beingdeleted (void) const { return beingdeleted.is_on (); }
  std::string get_beingdeleted (void) const { return beingdeleted.current_value (); }

  bool busyaction_is (const std::string& v) const { return busyaction.is (v); }
  std::string get_busyaction (void) const { return busyaction.current_value (); }

  void execute_buttondownfcn (const octave_value& data = octave_value ()) const { buttondownfcn.execute (data); }
  octave_value get_buttondownfcn (void) const { return buttondownfcn.get (); }

  Matrix get_children (void) const;

  bool is_clipping (void) const { return clipping.is_on (); }
  std::string get_clipping (void) const { return clipping.current_value (); }

  void execute_createfcn (const octave_value& data = octave_value ()) const { createfcn.execute (data); }
  octave_value get_createfcn (void) const { return createfcn.get (); }

  void execute_deletefcn (const octave_value& data = octave_value ()) const { deletefcn.execute (data); }
  octave_value get_deletefcn (void) const { return deletefcn.get (); }

  bool handlevisibility_is (const std::string& v) const { return handlevisibility.is (v); }
  std::string get_handlevisibility (void) const { return handlevisibility.current_value (); }

  bool is_hittest (void) const { return hittest.is_on (); }
  std::string get_hittest (void) const { return hittest.current_value (); }

  bool is_interruptible (void) const { return interruptible.is_on (); }
  std::string get_interruptible (void) const { return interruptible.current_value (); }

  graphics_handle get_parent (void) const { return parent.handle_value (); }

  bool is_selected (void) const { return selected.is_on (); }
  std::string get_selected (void) const { return selected.current_value (); }

  bool is_selectionhighlight (void) const { return selectionhighlight.is_on (); }
  std::string get_selectionhighlight (void) const { return selectionhighlight.current_value (); }

  std::string get_tag (void) const { return tag.string_value (); }

  std::string get_type (void) const { return type.string_value (); }

  octave_value get_userdata (void) const { return userdata.get (); }

  bool is_visible (void) const { return visible.is_on (); }
  std::string get_visible (void) const { return visible.current_value (); }

  bool is___modified__ (void) const { return __modified__.is_on (); }
  std::string get___modified__ (void) const { return __modified__.current_value (); }

  graphics_handle get___myhandle__ (void) const { return __myhandle__; }

  graphics_handle get_uicontextmenu (void) const { return uicontextmenu.handle_value (); }


  void set_beingdeleted (const octave_value& val)
  {
    if (! error_state)
      {
        if (beingdeleted.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_busyaction (const octave_value& val)
  {
    if (! error_state)
      {
        if (busyaction.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_buttondownfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (buttondownfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_clipping (const octave_value& val)
  {
    if (! error_state)
      {
        if (clipping.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_createfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (createfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_deletefcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (deletefcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_handlevisibility (const octave_value& val)
  {
    if (! error_state)
      {
        if (handlevisibility.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_hittest (const octave_value& val)
  {
    if (! error_state)
      {
        if (hittest.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interruptible (const octave_value& val)
  {
    if (! error_state)
      {
        if (interruptible.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_selected (const octave_value& val)
  {
    if (! error_state)
      {
        if (selected.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_selectionhighlight (const octave_value& val)
  {
    if (! error_state)
      {
        if (selectionhighlight.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_userdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (userdata.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_visible (const octave_value& val)
  {
    if (! error_state)
      {
        if (visible.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_uicontextmenu (const octave_value& val)
  {
    if (! error_state)
      {
        if (uicontextmenu.set (val, true))
          {
            mark_modified ();
          }
      }
  }


protected:
  struct cmp_caseless_str 
    {
      bool operator () (const caseless_str &a, const caseless_str &b) const
        {
	  std::string a1 = a;
	  std::transform (a1.begin (), a1.end (), a1.begin (), tolower);
	  std::string b1 = b;
	  std::transform (b1.begin (), b1.end (), b1.begin (), tolower);

          return a1 < b1;
        }
    };

  std::map<caseless_str, property, cmp_caseless_str> all_props;

protected:
  void insert_static_property (const std::string& name, base_property& p)
    { insert_property (name, property (&p, true)); }
  
  virtual void init (void) { }
};

class OCTINTERP_API base_graphics_object
{
public:
  friend class graphics_object;

  base_graphics_object (void) : count (1) { }

  base_graphics_object (const base_graphics_object&) { }

  virtual ~base_graphics_object (void) { }

  virtual void mark_modified (void)
  {
    if (valid_object ())
      get_properties ().mark_modified ();
    else
      error ("base_graphics_object::mark_modified: invalid graphics object");
  }

  virtual void override_defaults (base_graphics_object& obj)
  {
    if (valid_object ())
      get_properties ().override_defaults (obj);
    else
      error ("base_graphics_object::override_defaults: invalid graphics object");
  }

  virtual void set_from_list (property_list& plist)
  {
    if (valid_object ())
      get_properties ().set_from_list (*this, plist);
    else
      error ("base_graphics_object::set_from_list: invalid graphics object");
  }

  virtual void set (const caseless_str& pname, const octave_value& pval)
  {
    if (valid_object ())
      get_properties ().set (pname, pval);
    else
      error ("base_graphics_object::set: invalid graphics object");
  }

  virtual void set_defaults (const std::string&)
  {
    error ("base_graphics_object::set_defaults: invalid graphics object");
  }

  virtual octave_value get (bool all = false) const
  {
    if (valid_object ())
      return get_properties ().get (all);
    else
      {
        error ("base_graphics_object::get: invalid graphics object");
        return octave_value ();
      }
  }

  virtual octave_value get (const caseless_str& pname) const
  {
    if (valid_object ())
      return get_properties ().get (pname);
    else
      {
        error ("base_graphics_object::get: invalid graphics object");
        return octave_value ();
      }
  }

  virtual octave_value get_default (const caseless_str&) const;

  virtual octave_value get_factory_default (const caseless_str&) const;

  virtual octave_value get_defaults (void) const
  {
    error ("base_graphics_object::get_defaults: invalid graphics object");
    return octave_value ();
  }

  virtual octave_value get_factory_defaults (void) const
  {
    error ("base_graphics_object::get_factory_defaults: invalid graphics object");
    return octave_value ();
  }

  virtual graphics_handle get_parent (void) const
  {
    if (valid_object ())
      return get_properties ().get_parent ();
    else
      {
        error ("base_graphics_object::get_parent: invalid graphics object");
        return graphics_handle ();
      }
  }

  graphics_handle get_handle (void) const
  {
    if (valid_object ())
      return get_properties ().get___myhandle__ ();
    else
      {
        error ("base_graphics_object::get_handle: invalid graphics object");
        return graphics_handle ();
      }
  }

  virtual void remove_child (const graphics_handle& h)
  {
    if (valid_object ())
      get_properties ().remove_child (h);
    else
      error ("base_graphics_object::remove_child: invalid graphics object");
  }

  virtual void adopt (const graphics_handle& h)
  {
    if (valid_object ())
      get_properties ().adopt (h);
    else
      error ("base_graphics_object::adopt: invalid graphics object");
  }

  virtual void reparent (const graphics_handle& np)
  {
    if (valid_object ())
      get_properties ().reparent (np);
    else
      error ("base_graphics_object::reparent: invalid graphics object");
  }

  virtual void defaults (void) const
  {
    if (valid_object ())
      {
        std::string msg = (type () + "::defaults");
        gripe_not_implemented (msg.c_str ());
      }
    else
      error ("base_graphics_object::default: invalid graphics object");
  }

  virtual base_properties& get_properties (void)
  {
    static base_properties properties;
    error ("base_graphics_object::get_properties: invalid graphics object");
    return properties;
  }

  virtual const base_properties& get_properties (void) const
  {
    static base_properties properties;
    error ("base_graphics_object::get_properties: invalid graphics object");
    return properties;
  }

  virtual void update_axis_limits (const std::string& axis_type);

  virtual bool valid_object (void) const { return false; }

  virtual std::string type (void) const
  {
    return (valid_object () ? get_properties ().graphics_object_name ()
        : "unknown");
  }

  bool isa (const std::string& go_name) const
  {
    return type () == go_name;
  }

  virtual graphics_backend get_backend (void) const
  {
    if (valid_object ())
      return get_properties ().get_backend ();
    else
      {
	error ("base_graphics_object::get_backend: invalid graphics object");
	return graphics_backend ();
      }
  }

  virtual void add_property_listener (const std::string& nm,
				      const octave_value& v,
				      listener_mode mode = POSTSET)
    {
      if (valid_object ())
	get_properties ().add_listener (nm, v, mode);
    }

  virtual void delete_property_listener (const std::string& nm,
					 const octave_value& v,
					 listener_mode mode = POSTSET)
    {
      if (valid_object ())
	get_properties ().delete_listener (nm, v, mode);
    }

  virtual void remove_all_listeners (void);

protected:
  // A reference count.
  int count;
};

class OCTINTERP_API graphics_object
{
public:
  graphics_object (void) : rep (new base_graphics_object ()) { }

  graphics_object (base_graphics_object *new_rep)
    : rep (new_rep) { }

  graphics_object (const graphics_object& obj)
  {
    rep = obj.rep;
    rep->count++;
  }

  graphics_object& operator = (const graphics_object& obj)
  {
    if (rep != obj.rep)
      {
	if (--rep->count == 0)
	  delete rep;

	rep = obj.rep;
	rep->count++;
      }

    return *this;
  }

  ~graphics_object (void)
  {
    if (--rep->count == 0)
      delete rep;
  }

  void mark_modified (void) { rep->mark_modified (); }

  void override_defaults (base_graphics_object& obj)
  {
    rep->override_defaults (obj);
  }

  void set_from_list (property_list& plist) { rep->set_from_list (plist); }

  void set (const caseless_str& name, const octave_value& val)
  {
    rep->set (name, val);
  }

  void set (const octave_value_list& args);

  void set_defaults (const std::string& mode) { rep->set_defaults (mode); }

  octave_value get (bool all = false) const { return rep->get (all); }

  octave_value get (const caseless_str& name) const
  {
    return name.compare ("default")
      ? get_defaults ()
      : (name.compare ("factory")
	 ? get_factory_defaults () : rep->get (name));
  }

  octave_value get_default (const caseless_str& name) const
  {
    return rep->get_default (name);
  }

  octave_value get_factory_default (const caseless_str& name) const
  {
    return rep->get_factory_default (name);
  }

  octave_value get_defaults (void) const { return rep->get_defaults (); }

  octave_value get_factory_defaults (void) const
  {
    return rep->get_factory_defaults ();
  }

  graphics_handle get_parent (void) const { return rep->get_parent (); }

  graphics_handle get_handle (void) const { return rep->get_handle (); }

  void remove_child (const graphics_handle& h) { rep->remove_child (h); }

  void adopt (const graphics_handle& h) { rep->adopt (h); }

  void reparent (const graphics_handle& h) { rep->reparent (h); }

  void defaults (void) const { rep->defaults (); }

  bool isa (const std::string& go_name) const { return rep->isa (go_name); }

  base_properties& get_properties (void) { return rep->get_properties (); }

  const base_properties& get_properties (void) const
  {
    return rep->get_properties ();
  }

  void update_axis_limits (const std::string& axis_type)
  {
    rep->update_axis_limits (axis_type);
  }

  bool valid_object (void) const { return rep->valid_object (); }

  std::string type (void) const { return rep->type (); }

  operator bool (void) const { return rep->valid_object (); }

  // FIXME -- these functions should be generated automatically by the
  // genprops.awk script.
  //
  // EMIT_GRAPHICS_OBJECT_GET_FUNCTIONS

  octave_value get_xlim (void) const
  { return get_properties ().get_xlim (); }

  octave_value get_ylim (void) const
  { return get_properties ().get_ylim (); }
  
  octave_value get_zlim (void) const
  { return get_properties ().get_zlim (); }
  
  octave_value get_clim (void) const
  { return get_properties ().get_clim (); }
  
  octave_value get_alim (void) const
  { return get_properties ().get_alim (); }

  bool is_xliminclude (void) const
  { return get_properties ().is_xliminclude (); }
  
  bool is_yliminclude (void) const
  { return get_properties ().is_yliminclude (); }
  
  bool is_zliminclude (void) const
  { return get_properties ().is_zliminclude (); }
  
  bool is_climinclude (void) const
  { return get_properties ().is_climinclude (); }
  
  bool is_aliminclude (void) const
  { return get_properties ().is_aliminclude (); }

  bool is_handle_visible (void) const
  { return get_properties ().is_handle_visible (); }
  
  graphics_backend get_backend (void) const { return rep->get_backend (); }

  void add_property_listener (const std::string& nm, const octave_value& v,
			      listener_mode mode = POSTSET)
    { rep->add_property_listener (nm, v, mode); }

  void delete_property_listener (const std::string& nm, const octave_value& v,
				 listener_mode mode = POSTSET)
    { rep->delete_property_listener (nm, v, mode); }

private:
  base_graphics_object *rep;
};

// ---------------------------------------------------------------------

class OCTINTERP_API root_figure : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    void remove_child (const graphics_handle& h);
    
    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  handle_property currentfigure;
  handle_property callbackobject;
  double_property screendepth;
  array_property screensize;
  double_property screenpixelsperinch;
  radio_property units;
  bool_property showhiddenhandles;

public:

  enum
  {
    CURRENTFIGURE = 1000,
    CALLBACKOBJECT = 1001,
    SCREENDEPTH = 1002,
    SCREENSIZE = 1003,
    SCREENPIXELSPERINCH = 1004,
    UNITS = 1005,
    SHOWHIDDENHANDLES = 1006
  };

  graphics_handle get_currentfigure (void) const { return currentfigure.handle_value (); }

  graphics_handle get_callbackobject (void) const { return callbackobject.handle_value (); }

  double get_screendepth (void) const { return screendepth.double_value (); }

  octave_value get_screensize (void) const { return screensize.get (); }

  double get_screenpixelsperinch (void) const { return screenpixelsperinch.double_value (); }

  bool units_is (const std::string& v) const { return units.is (v); }
  std::string get_units (void) const { return units.current_value (); }

  bool is_showhiddenhandles (void) const { return showhiddenhandles.is_on (); }
  std::string get_showhiddenhandles (void) const { return showhiddenhandles.current_value (); }


  void set_currentfigure (const octave_value& val);

  void set_callbackobject (const octave_value& val);

  void set_screendepth (const octave_value& val)
  {
    if (! error_state)
      {
        if (screendepth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_screensize (const octave_value& val)
  {
    if (! error_state)
      {
        if (screensize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_screenpixelsperinch (const octave_value& val)
  {
    if (! error_state)
      {
        if (screenpixelsperinch.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_units (const octave_value& val)
  {
    if (! error_state)
      {
        if (units.set (val, true))
          {
            update_units ();
            mark_modified ();
          }
      }
  }

  void update_units (void);

  void set_showhiddenhandles (const octave_value& val)
  {
    if (! error_state)
      {
        if (showhiddenhandles.set (val, true))
          {
            mark_modified ();
          }
      }
  }


  private:
    std::list<graphics_handle> cbo_stack;
  };

private:
  properties xproperties;

public:

  root_figure (void) : xproperties (0, graphics_handle ()), default_properties () { }

  ~root_figure (void) { xproperties.delete_children (); }

  void mark_modified (void) { }

  void override_defaults (base_graphics_object& obj)
  {
    // Now override with our defaults.  If the default_properties
    // list includes the properties for all defaults (line,
    // surface, etc.) then we don't have to know the type of OBJ
    // here, we just call its set function and let it decide which
    // properties from the list to use.
    obj.set_from_list (default_properties);
  }

  void set (const caseless_str& name, const octave_value& value)
  {
    if (name.compare ("default", 7))
      // strip "default", pass rest to function that will
      // parse the remainder and add the element to the
      // default_properties map.
      default_properties.set (name.substr (7), value);
    else
      xproperties.set (name, value);
  }

  octave_value get (const caseless_str& name) const
  {
    octave_value retval;

    if (name.compare ("default", 7))
      return get_default (name.substr (7));
    else if (name.compare ("factory", 7))
      return get_factory_default (name.substr (7));
    else
      retval = xproperties.get (name);

    return retval;
  }

  octave_value get_default (const caseless_str& name) const
  {
    octave_value retval = default_properties.lookup (name);

    if (retval.is_undefined ())
      {
	// no default property found, use factory default
	retval = factory_properties.lookup (name);

	if (retval.is_undefined ())
	  error ("get: invalid default property `%s'", name.c_str ());
      }

    return retval;
  }

  octave_value get_factory_default (const caseless_str& name) const
  {
    octave_value retval = factory_properties.lookup (name);

    if (retval.is_undefined ())
      error ("get: invalid factory default property `%s'", name.c_str ());

    return retval;
  }

  octave_value get_defaults (void) const
  {
    return default_properties.as_struct ("default");
  }

  octave_value get_factory_defaults (void) const
  {
    return factory_properties.as_struct ("factory");
  }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }

private:
  property_list default_properties;

  static property_list factory_properties;

  static property_list::plist_map_type init_factory_properties (void);
};

// ---------------------------------------------------------------------

class OCTINTERP_API figure : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    void remove_child (const graphics_handle& h);

    void set_visible (const octave_value& val);

    graphics_backend get_backend (void) const
      {
	if (! backend)
	  backend = graphics_backend::default_backend ();

	return backend;
      }

    void set_backend (const graphics_backend& b) 
    { 
      if (backend)
	backend.object_destroyed (__myhandle__);
      backend = b; 
      __backend__ = b.get_name ();
      __plot_stream__ = Matrix ();
      mark_modified ();
    }

    void set___backend__ (const octave_value& val)
    {
      if (! error_state)
	{
	  if (val.is_string ())
	    {
	      std::string nm = val.string_value ();
	      graphics_backend b = graphics_backend::find_backend (nm);
	      if (b.get_name () != nm)
		{
		  error ("set___backend__: invalid backend");
		}
	      else
		{
		  set_backend (b);
		  mark_modified ();
		}
	    }
	  else
	    error ("set___backend__ must be a string");
	}
    }

    Matrix get_boundingbox (bool internal = false) const;

    void set_boundingbox (const Matrix& bb);

    std::string get_title (void) const;

    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  any_property __plot_stream__;
  bool_property __enhanced__;
  radio_property nextplot;
  callback_property closerequestfcn;
  handle_property currentaxes;
  array_property colormap;
  radio_property paperorientation;
  color_property color;
  array_property alphamap;
  string_property currentcharacter;
  handle_property currentobject;
  array_property current_point;
  bool_property dockcontrols;
  bool_property doublebuffer;
  string_property filename;
  bool_property integerhandle;
  bool_property inverthardcopy;
  callback_property keypressfcn;
  callback_property keyreleasefcn;
  radio_property menubar;
  double_property mincolormap;
  string_property name;
  bool_property numbertitle;
  radio_property paperunits;
  array_property paperposition;
  radio_property paperpositionmode;
  array_property papersize;
  radio_property papertype;
  radio_property pointer;
  array_property pointershapecdata;
  array_property pointershapehotspot;
  array_property position;
  radio_property renderer;
  radio_property renderermode;
  bool_property resize;
  callback_property resizefcn;
  radio_property selectiontype;
  radio_property toolbar;
  radio_property units;
  callback_property windowbuttondownfcn;
  callback_property windowbuttonmotionfcn;
  callback_property windowbuttonupfcn;
  callback_property windowbuttonwheelfcn;
  radio_property windowstyle;
  string_property wvisual;
  radio_property wvisualmode;
  string_property xdisplay;
  string_property xvisual;
  radio_property xvisualmode;
  callback_property buttondownfcn;
  string_property __backend__;

public:

  enum
  {
    __PLOT_STREAM__ = 2000,
    __ENHANCED__ = 2001,
    NEXTPLOT = 2002,
    CLOSEREQUESTFCN = 2003,
    CURRENTAXES = 2004,
    COLORMAP = 2005,
    PAPERORIENTATION = 2006,
    COLOR = 2007,
    ALPHAMAP = 2008,
    CURRENTCHARACTER = 2009,
    CURRENTOBJECT = 2010,
    CURRENT_POINT = 2011,
    DOCKCONTROLS = 2012,
    DOUBLEBUFFER = 2013,
    FILENAME = 2014,
    INTEGERHANDLE = 2015,
    INVERTHARDCOPY = 2016,
    KEYPRESSFCN = 2017,
    KEYRELEASEFCN = 2018,
    MENUBAR = 2019,
    MINCOLORMAP = 2020,
    NAME = 2021,
    NUMBERTITLE = 2022,
    PAPERUNITS = 2023,
    PAPERPOSITION = 2024,
    PAPERPOSITIONMODE = 2025,
    PAPERSIZE = 2026,
    PAPERTYPE = 2027,
    POINTER = 2028,
    POINTERSHAPECDATA = 2029,
    POINTERSHAPEHOTSPOT = 2030,
    POSITION = 2031,
    RENDERER = 2032,
    RENDERERMODE = 2033,
    RESIZE = 2034,
    RESIZEFCN = 2035,
    SELECTIONTYPE = 2036,
    TOOLBAR = 2037,
    UNITS = 2038,
    WINDOWBUTTONDOWNFCN = 2039,
    WINDOWBUTTONMOTIONFCN = 2040,
    WINDOWBUTTONUPFCN = 2041,
    WINDOWBUTTONWHEELFCN = 2042,
    WINDOWSTYLE = 2043,
    WVISUAL = 2044,
    WVISUALMODE = 2045,
    XDISPLAY = 2046,
    XVISUAL = 2047,
    XVISUALMODE = 2048,
    BUTTONDOWNFCN = 2049,
    __BACKEND__ = 2050
  };

  octave_value get___plot_stream__ (void) const { return __plot_stream__.get (); }

  bool is___enhanced__ (void) const { return __enhanced__.is_on (); }
  std::string get___enhanced__ (void) const { return __enhanced__.current_value (); }

  bool nextplot_is (const std::string& v) const { return nextplot.is (v); }
  std::string get_nextplot (void) const { return nextplot.current_value (); }

  void execute_closerequestfcn (const octave_value& data = octave_value ()) const { closerequestfcn.execute (data); }
  octave_value get_closerequestfcn (void) const { return closerequestfcn.get (); }

  graphics_handle get_currentaxes (void) const { return currentaxes.handle_value (); }

  octave_value get_colormap (void) const { return colormap.get (); }

  bool paperorientation_is (const std::string& v) const { return paperorientation.is (v); }
  std::string get_paperorientation (void) const { return paperorientation.current_value (); }

  bool color_is_rgb (void) const { return color.is_rgb (); }
  bool color_is (const std::string& v) const { return color.is (v); }
  Matrix get_color_rgb (void) const { return (color.is_rgb () ? color.rgb () : Matrix ()); }
  octave_value get_color (void) const { return color.get (); }

  octave_value get_alphamap (void) const { return alphamap.get (); }

  std::string get_currentcharacter (void) const { return currentcharacter.string_value (); }

  graphics_handle get_currentobject (void) const { return currentobject.handle_value (); }

  octave_value get_current_point (void) const { return current_point.get (); }

  bool is_dockcontrols (void) const { return dockcontrols.is_on (); }
  std::string get_dockcontrols (void) const { return dockcontrols.current_value (); }

  bool is_doublebuffer (void) const { return doublebuffer.is_on (); }
  std::string get_doublebuffer (void) const { return doublebuffer.current_value (); }

  std::string get_filename (void) const { return filename.string_value (); }

  bool is_integerhandle (void) const { return integerhandle.is_on (); }
  std::string get_integerhandle (void) const { return integerhandle.current_value (); }

  bool is_inverthardcopy (void) const { return inverthardcopy.is_on (); }
  std::string get_inverthardcopy (void) const { return inverthardcopy.current_value (); }

  void execute_keypressfcn (const octave_value& data = octave_value ()) const { keypressfcn.execute (data); }
  octave_value get_keypressfcn (void) const { return keypressfcn.get (); }

  void execute_keyreleasefcn (const octave_value& data = octave_value ()) const { keyreleasefcn.execute (data); }
  octave_value get_keyreleasefcn (void) const { return keyreleasefcn.get (); }

  bool menubar_is (const std::string& v) const { return menubar.is (v); }
  std::string get_menubar (void) const { return menubar.current_value (); }

  double get_mincolormap (void) const { return mincolormap.double_value (); }

  std::string get_name (void) const { return name.string_value (); }

  bool is_numbertitle (void) const { return numbertitle.is_on (); }
  std::string get_numbertitle (void) const { return numbertitle.current_value (); }

  bool paperunits_is (const std::string& v) const { return paperunits.is (v); }
  std::string get_paperunits (void) const { return paperunits.current_value (); }

  octave_value get_paperposition (void) const { return paperposition.get (); }

  bool paperpositionmode_is (const std::string& v) const { return paperpositionmode.is (v); }
  std::string get_paperpositionmode (void) const { return paperpositionmode.current_value (); }

  octave_value get_papersize (void) const { return papersize.get (); }

  bool papertype_is (const std::string& v) const { return papertype.is (v); }
  std::string get_papertype (void) const { return papertype.current_value (); }

  bool pointer_is (const std::string& v) const { return pointer.is (v); }
  std::string get_pointer (void) const { return pointer.current_value (); }

  octave_value get_pointershapecdata (void) const { return pointershapecdata.get (); }

  octave_value get_pointershapehotspot (void) const { return pointershapehotspot.get (); }

  octave_value get_position (void) const { return position.get (); }

  bool renderer_is (const std::string& v) const { return renderer.is (v); }
  std::string get_renderer (void) const { return renderer.current_value (); }

  bool renderermode_is (const std::string& v) const { return renderermode.is (v); }
  std::string get_renderermode (void) const { return renderermode.current_value (); }

  bool is_resize (void) const { return resize.is_on (); }
  std::string get_resize (void) const { return resize.current_value (); }

  void execute_resizefcn (const octave_value& data = octave_value ()) const { resizefcn.execute (data); }
  octave_value get_resizefcn (void) const { return resizefcn.get (); }

  bool selectiontype_is (const std::string& v) const { return selectiontype.is (v); }
  std::string get_selectiontype (void) const { return selectiontype.current_value (); }

  bool toolbar_is (const std::string& v) const { return toolbar.is (v); }
  std::string get_toolbar (void) const { return toolbar.current_value (); }

  bool units_is (const std::string& v) const { return units.is (v); }
  std::string get_units (void) const { return units.current_value (); }

  void execute_windowbuttondownfcn (const octave_value& data = octave_value ()) const { windowbuttondownfcn.execute (data); }
  octave_value get_windowbuttondownfcn (void) const { return windowbuttondownfcn.get (); }

  void execute_windowbuttonmotionfcn (const octave_value& data = octave_value ()) const { windowbuttonmotionfcn.execute (data); }
  octave_value get_windowbuttonmotionfcn (void) const { return windowbuttonmotionfcn.get (); }

  void execute_windowbuttonupfcn (const octave_value& data = octave_value ()) const { windowbuttonupfcn.execute (data); }
  octave_value get_windowbuttonupfcn (void) const { return windowbuttonupfcn.get (); }

  void execute_windowbuttonwheelfcn (const octave_value& data = octave_value ()) const { windowbuttonwheelfcn.execute (data); }
  octave_value get_windowbuttonwheelfcn (void) const { return windowbuttonwheelfcn.get (); }

  bool windowstyle_is (const std::string& v) const { return windowstyle.is (v); }
  std::string get_windowstyle (void) const { return windowstyle.current_value (); }

  std::string get_wvisual (void) const { return wvisual.string_value (); }

  bool wvisualmode_is (const std::string& v) const { return wvisualmode.is (v); }
  std::string get_wvisualmode (void) const { return wvisualmode.current_value (); }

  std::string get_xdisplay (void) const { return xdisplay.string_value (); }

  std::string get_xvisual (void) const { return xvisual.string_value (); }

  bool xvisualmode_is (const std::string& v) const { return xvisualmode.is (v); }
  std::string get_xvisualmode (void) const { return xvisualmode.current_value (); }

  void execute_buttondownfcn (const octave_value& data = octave_value ()) const { buttondownfcn.execute (data); }
  octave_value get_buttondownfcn (void) const { return buttondownfcn.get (); }

  std::string get___backend__ (void) const { return __backend__.string_value (); }


  void set___plot_stream__ (const octave_value& val)
  {
    if (! error_state)
      {
        if (__plot_stream__.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set___enhanced__ (const octave_value& val)
  {
    if (! error_state)
      {
        if (__enhanced__.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_nextplot (const octave_value& val)
  {
    if (! error_state)
      {
        if (nextplot.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_closerequestfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (closerequestfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_currentaxes (const octave_value& val);

  void set_colormap (const octave_value& val)
  {
    if (! error_state)
      {
        if (colormap.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_paperorientation (const octave_value& val)
  {
    if (! error_state)
      {
        if (paperorientation.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_color (const octave_value& val)
  {
    if (! error_state)
      {
        if (color.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_alphamap (const octave_value& val)
  {
    if (! error_state)
      {
        if (alphamap.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_currentcharacter (const octave_value& val)
  {
    if (! error_state)
      {
        if (currentcharacter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_currentobject (const octave_value& val)
  {
    if (! error_state)
      {
        if (currentobject.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_current_point (const octave_value& val)
  {
    if (! error_state)
      {
        if (current_point.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_dockcontrols (const octave_value& val)
  {
    if (! error_state)
      {
        if (dockcontrols.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_doublebuffer (const octave_value& val)
  {
    if (! error_state)
      {
        if (doublebuffer.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_filename (const octave_value& val)
  {
    if (! error_state)
      {
        if (filename.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_integerhandle (const octave_value& val)
  {
    if (! error_state)
      {
        if (integerhandle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_inverthardcopy (const octave_value& val)
  {
    if (! error_state)
      {
        if (inverthardcopy.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keypressfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (keypressfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keyreleasefcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (keyreleasefcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_menubar (const octave_value& val)
  {
    if (! error_state)
      {
        if (menubar.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_mincolormap (const octave_value& val)
  {
    if (! error_state)
      {
        if (mincolormap.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_name (const octave_value& val)
  {
    if (! error_state)
      {
        if (name.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_numbertitle (const octave_value& val)
  {
    if (! error_state)
      {
        if (numbertitle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_paperunits (const octave_value& val)
  {
    if (! error_state)
      {
        if (paperunits.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_paperposition (const octave_value& val)
  {
    if (! error_state)
      {
        if (paperposition.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_paperpositionmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (paperpositionmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_papersize (const octave_value& val)
  {
    if (! error_state)
      {
        if (papersize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_papertype (const octave_value& val)
  {
    if (! error_state)
      {
        if (papertype.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_pointer (const octave_value& val)
  {
    if (! error_state)
      {
        if (pointer.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_pointershapecdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (pointershapecdata.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_pointershapehotspot (const octave_value& val)
  {
    if (! error_state)
      {
        if (pointershapehotspot.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_position (const octave_value& val);

  void set_renderer (const octave_value& val)
  {
    if (! error_state)
      {
        if (renderer.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_renderermode (const octave_value& val)
  {
    if (! error_state)
      {
        if (renderermode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_resize (const octave_value& val)
  {
    if (! error_state)
      {
        if (resize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_resizefcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (resizefcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_selectiontype (const octave_value& val)
  {
    if (! error_state)
      {
        if (selectiontype.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_toolbar (const octave_value& val)
  {
    if (! error_state)
      {
        if (toolbar.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_units (const octave_value& val)
  {
    if (! error_state)
      {
        if (units.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_windowbuttondownfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (windowbuttondownfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_windowbuttonmotionfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (windowbuttonmotionfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_windowbuttonupfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (windowbuttonupfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_windowbuttonwheelfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (windowbuttonwheelfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_windowstyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (windowstyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_wvisual (const octave_value& val)
  {
    if (! error_state)
      {
        if (wvisual.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_wvisualmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (wvisualmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xdisplay (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdisplay.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xvisual (const octave_value& val)
  {
    if (! error_state)
      {
        if (xvisual.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xvisualmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (xvisualmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_buttondownfcn (const octave_value& val)
  {
    if (! error_state)
      {
        if (buttondownfcn.set (val, true))
          {
            mark_modified ();
          }
      }
  }

    
  protected:
    void init (void)
      {
        colormap.add_constraint (dim_vector (-1, 3));
	alphamap.add_constraint (dim_vector (-1, 1));
	paperposition.add_constraint (dim_vector (1, 4));
	pointershapecdata.add_constraint (dim_vector (16, 16));
	pointershapehotspot.add_constraint (dim_vector (1, 2));
	position.add_constraint (dim_vector (1, 4));
      }

  private:
    mutable graphics_backend backend;
  };

private:
  properties xproperties;

public:
  figure (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p), default_properties ()
  {
    xproperties.override_defaults (*this);
  }

  ~figure (void)
  {
    xproperties.delete_children (); 
  }

  void override_defaults (base_graphics_object& obj)
  {
    // Allow parent (root figure) to override first (properties knows how
    // to find the parent object).
    xproperties.override_defaults (obj);

    // Now override with our defaults.  If the default_properties
    // list includes the properties for all defaults (line,
    // surface, etc.) then we don't have to know the type of OBJ
    // here, we just call its set function and let it decide which
    // properties from the list to use.
    obj.set_from_list (default_properties);
  }

  void set (const caseless_str& name, const octave_value& value)
  {
    if (name.compare ("default", 7))
      // strip "default", pass rest to function that will
      // parse the remainder and add the element to the
      // default_properties map.
      default_properties.set (name.substr (7), value);
    else
      xproperties.set (name, value);
  }

  octave_value get (const caseless_str& name) const
  {
    octave_value retval;

    if (name.compare ("default", 7))
      retval = get_default (name.substr (7));
    else
      retval = xproperties.get (name);

    return retval;
  }

  octave_value get_default (const caseless_str& name) const;

  octave_value get_defaults (void) const
  {
    return default_properties.as_struct ("default");
  }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }

private:
  property_list default_properties;
};

// ---------------------------------------------------------------------

class OCTINTERP_API graphics_xform
{
public:
  graphics_xform (void)
      : xform (xform_eye ()), xform_inv (xform_eye ())
    {
      sx = sy = sz = "linear";
    }

  graphics_xform (const Matrix& xm, const Matrix& xim,
		  const scaler& x, const scaler& y, const scaler& z)
      : xform (xm), xform_inv (xim), sx (x), sy (y), sz (z) { }

  graphics_xform (const graphics_xform& g)
      : xform (g.xform), xform_inv (g.xform_inv), sx (g.sx),
        sy (g.sy), sz (g.sz) { }

  ~graphics_xform (void) { }

  graphics_xform& operator = (const graphics_xform& g)
    {
      xform = g.xform;
      xform_inv = g.xform_inv;
      sx = g.sx;
      sy = g.sy;
      sz = g.sz;

      return *this;
    }

  static ColumnVector xform_vector (double x, double y, double z);

  static Matrix xform_eye (void);

  ColumnVector transform (double x, double y, double z,
			  bool scale = true) const;
  
  ColumnVector untransform (double x, double y, double z,
			    bool scale = true) const;

  Matrix xscale (const Matrix& m) const { return sx.scale (m); }
  Matrix yscale (const Matrix& m) const { return sy.scale (m); }
  Matrix zscale (const Matrix& m) const { return sz.scale (m); }

  Matrix scale (const Matrix& m) const
    {
      bool has_z = (m.columns () > 2);

      if (sx.is_linear () && sy.is_linear ()
	  && (! has_z || sz.is_linear ()))
	return m;

      Matrix retval (m.dims ());

      int r = m.rows ();

      for (int i = 0; i < r; i++)
	{
	  retval(i,0) = sx.scale (m(i,0));
	  retval(i,1) = sy.scale (m(i,1));
	  if (has_z)
	    retval(i,2) = sz.scale (m(i,2));
	}

      return retval;
    }

private:
  Matrix xform;
  Matrix xform_inv;
  scaler sx, sy, sz;
};

class OCTINTERP_API axes : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    void set_defaults (base_graphics_object& obj, const std::string& mode);

    void remove_child (const graphics_handle& h);

    const scaler& get_x_scaler (void) const { return sx; }
    const scaler& get_y_scaler (void) const { return sy; }
    const scaler& get_z_scaler (void) const { return sz; }

    Matrix get_boundingbox (bool internal = false) const;

    void update_boundingbox (void)
      {
	if (units_is ("normalized"))
	  {
	    update_transform ();
	    base_properties::update_boundingbox ();
	  }
      }

    void update_camera (void);
    void update_aspectratios (void);
    void update_transform (void)
      {
	update_aspectratios ();
	update_camera ();
      }

    graphics_xform get_transform (void) const
      { return graphics_xform (x_render, x_render_inv, sx, sy, sz); }

    Matrix get_transform_matrix (void) const { return x_render; }
    Matrix get_inverse_transform_matrix (void) const { return x_render_inv; }
    Matrix get_opengl_matrix_1 (void) const { return x_gl_mat1; }
    Matrix get_opengl_matrix_2 (void) const { return x_gl_mat2; }
    Matrix get_transform_zlim (void) const { return x_zlim; }

    ColumnVector pixel2coord (double px, double py) const
    { return get_transform ().untransform (px, py, (x_zlim(0)+x_zlim(1))/2); }

    ColumnVector coord2pixel (double x, double y, double z) const
    { return get_transform ().transform (x, y, z); }

    void zoom (const Matrix& xl, const Matrix& yl);
    void unzoom (void);
    void clear_zoom_stack (void);

  private:
    scaler sx, sy, sz;
    Matrix x_render, x_render_inv;
    Matrix x_gl_mat1, x_gl_mat2;
    Matrix x_zlim;
    std::list<octave_value> zoom_stack;

    void set_text_child (handle_property& h, const std::string& who,
			 const octave_value& v);

    void delete_text_child (handle_property& h);

    // See the genprops.awk script for an explanation of the
    // properties declarations.

    // properties which are not in matlab: interpreter

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  array_property position;
  bool_property box;
  bool_property key;
  bool_property keybox;
  bool_property keyreverse;
  any_property keypos;
  array_property colororder;
  array_property dataaspectratio;
  radio_property dataaspectratiomode;
  radio_property layer;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  row_vector_property clim;
  row_vector_property alim;
  radio_property xlimmode;
  radio_property ylimmode;
  radio_property zlimmode;
  radio_property climmode;
  radio_property alimmode;
  handle_property xlabel;
  handle_property ylabel;
  handle_property zlabel;
  handle_property title;
  bool_property xgrid;
  bool_property ygrid;
  bool_property zgrid;
  bool_property xminorgrid;
  bool_property yminorgrid;
  bool_property zminorgrid;
  row_vector_property xtick;
  row_vector_property ytick;
  row_vector_property ztick;
  radio_property xtickmode;
  radio_property ytickmode;
  radio_property ztickmode;
  bool_property xminortick;
  bool_property yminortick;
  bool_property zminortick;
  any_property xticklabel;
  any_property yticklabel;
  any_property zticklabel;
  radio_property xticklabelmode;
  radio_property yticklabelmode;
  radio_property zticklabelmode;
  radio_property interpreter;
  color_property color;
  color_property xcolor;
  color_property ycolor;
  color_property zcolor;
  radio_property xscale;
  radio_property yscale;
  radio_property zscale;
  radio_property xdir;
  radio_property ydir;
  radio_property zdir;
  radio_property yaxislocation;
  radio_property xaxislocation;
  array_property view;
  radio_property nextplot;
  array_property outerposition;
  radio_property activepositionproperty;
  color_property ambientlightcolor;
  array_property cameraposition;
  array_property cameratarget;
  array_property cameraupvector;
  double_property cameraviewangle;
  radio_property camerapositionmode;
  radio_property cameratargetmode;
  radio_property cameraupvectormode;
  radio_property cameraviewanglemode;
  array_property currentpoint;
  radio_property drawmode;
  radio_property fontangle;
  string_property fontname;
  double_property fontsize;
  radio_property fontunits;
  radio_property fontweight;
  radio_property gridlinestyle;
  string_property linestyleorder;
  double_property linewidth;
  radio_property minorgridlinestyle;
  array_property plotboxaspectratio;
  radio_property plotboxaspectratiomode;
  radio_property projection;
  radio_property tickdir;
  radio_property tickdirmode;
  array_property ticklength;
  array_property tightinset;
  radio_property units;
  array_property x_viewtransform;
  array_property x_projectiontransform;
  array_property x_viewporttransform;
  array_property x_normrendertransform;
  array_property x_rendertransform;

public:

  enum
  {
    POSITION = 3000,
    BOX = 3001,
    KEY = 3002,
    KEYBOX = 3003,
    KEYREVERSE = 3004,
    KEYPOS = 3005,
    COLORORDER = 3006,
    DATAASPECTRATIO = 3007,
    DATAASPECTRATIOMODE = 3008,
    LAYER = 3009,
    XLIM = 3010,
    YLIM = 3011,
    ZLIM = 3012,
    CLIM = 3013,
    ALIM = 3014,
    XLIMMODE = 3015,
    YLIMMODE = 3016,
    ZLIMMODE = 3017,
    CLIMMODE = 3018,
    ALIMMODE = 3019,
    XLABEL = 3020,
    YLABEL = 3021,
    ZLABEL = 3022,
    TITLE = 3023,
    XGRID = 3024,
    YGRID = 3025,
    ZGRID = 3026,
    XMINORGRID = 3027,
    YMINORGRID = 3028,
    ZMINORGRID = 3029,
    XTICK = 3030,
    YTICK = 3031,
    ZTICK = 3032,
    XTICKMODE = 3033,
    YTICKMODE = 3034,
    ZTICKMODE = 3035,
    XMINORTICK = 3036,
    YMINORTICK = 3037,
    ZMINORTICK = 3038,
    XTICKLABEL = 3039,
    YTICKLABEL = 3040,
    ZTICKLABEL = 3041,
    XTICKLABELMODE = 3042,
    YTICKLABELMODE = 3043,
    ZTICKLABELMODE = 3044,
    INTERPRETER = 3045,
    COLOR = 3046,
    XCOLOR = 3047,
    YCOLOR = 3048,
    ZCOLOR = 3049,
    XSCALE = 3050,
    YSCALE = 3051,
    ZSCALE = 3052,
    XDIR = 3053,
    YDIR = 3054,
    ZDIR = 3055,
    YAXISLOCATION = 3056,
    XAXISLOCATION = 3057,
    VIEW = 3058,
    NEXTPLOT = 3059,
    OUTERPOSITION = 3060,
    ACTIVEPOSITIONPROPERTY = 3061,
    AMBIENTLIGHTCOLOR = 3062,
    CAMERAPOSITION = 3063,
    CAMERATARGET = 3064,
    CAMERAUPVECTOR = 3065,
    CAMERAVIEWANGLE = 3066,
    CAMERAPOSITIONMODE = 3067,
    CAMERATARGETMODE = 3068,
    CAMERAUPVECTORMODE = 3069,
    CAMERAVIEWANGLEMODE = 3070,
    CURRENTPOINT = 3071,
    DRAWMODE = 3072,
    FONTANGLE = 3073,
    FONTNAME = 3074,
    FONTSIZE = 3075,
    FONTUNITS = 3076,
    FONTWEIGHT = 3077,
    GRIDLINESTYLE = 3078,
    LINESTYLEORDER = 3079,
    LINEWIDTH = 3080,
    MINORGRIDLINESTYLE = 3081,
    PLOTBOXASPECTRATIO = 3082,
    PLOTBOXASPECTRATIOMODE = 3083,
    PROJECTION = 3084,
    TICKDIR = 3085,
    TICKDIRMODE = 3086,
    TICKLENGTH = 3087,
    TIGHTINSET = 3088,
    UNITS = 3089,
    X_VIEWTRANSFORM = 3090,
    X_PROJECTIONTRANSFORM = 3091,
    X_VIEWPORTTRANSFORM = 3092,
    X_NORMRENDERTRANSFORM = 3093,
    X_RENDERTRANSFORM = 3094
  };

  octave_value get_position (void) const { return position.get (); }

  bool is_box (void) const { return box.is_on (); }
  std::string get_box (void) const { return box.current_value (); }

  bool is_key (void) const { return key.is_on (); }
  std::string get_key (void) const { return key.current_value (); }

  bool is_keybox (void) const { return keybox.is_on (); }
  std::string get_keybox (void) const { return keybox.current_value (); }

  bool is_keyreverse (void) const { return keyreverse.is_on (); }
  std::string get_keyreverse (void) const { return keyreverse.current_value (); }

  octave_value get_keypos (void) const { return keypos.get (); }

  octave_value get_colororder (void) const { return colororder.get (); }

  octave_value get_dataaspectratio (void) const { return dataaspectratio.get (); }

  bool dataaspectratiomode_is (const std::string& v) const { return dataaspectratiomode.is (v); }
  std::string get_dataaspectratiomode (void) const { return dataaspectratiomode.current_value (); }

  bool layer_is (const std::string& v) const { return layer.is (v); }
  std::string get_layer (void) const { return layer.current_value (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  octave_value get_clim (void) const { return clim.get (); }

  octave_value get_alim (void) const { return alim.get (); }

  bool xlimmode_is (const std::string& v) const { return xlimmode.is (v); }
  std::string get_xlimmode (void) const { return xlimmode.current_value (); }

  bool ylimmode_is (const std::string& v) const { return ylimmode.is (v); }
  std::string get_ylimmode (void) const { return ylimmode.current_value (); }

  bool zlimmode_is (const std::string& v) const { return zlimmode.is (v); }
  std::string get_zlimmode (void) const { return zlimmode.current_value (); }

  bool climmode_is (const std::string& v) const { return climmode.is (v); }
  std::string get_climmode (void) const { return climmode.current_value (); }

  bool alimmode_is (const std::string& v) const { return alimmode.is (v); }
  std::string get_alimmode (void) const { return alimmode.current_value (); }

  graphics_handle get_xlabel (void) const { return xlabel.handle_value (); }

  graphics_handle get_ylabel (void) const { return ylabel.handle_value (); }

  graphics_handle get_zlabel (void) const { return zlabel.handle_value (); }

  graphics_handle get_title (void) const { return title.handle_value (); }

  bool is_xgrid (void) const { return xgrid.is_on (); }
  std::string get_xgrid (void) const { return xgrid.current_value (); }

  bool is_ygrid (void) const { return ygrid.is_on (); }
  std::string get_ygrid (void) const { return ygrid.current_value (); }

  bool is_zgrid (void) const { return zgrid.is_on (); }
  std::string get_zgrid (void) const { return zgrid.current_value (); }

  bool is_xminorgrid (void) const { return xminorgrid.is_on (); }
  std::string get_xminorgrid (void) const { return xminorgrid.current_value (); }

  bool is_yminorgrid (void) const { return yminorgrid.is_on (); }
  std::string get_yminorgrid (void) const { return yminorgrid.current_value (); }

  bool is_zminorgrid (void) const { return zminorgrid.is_on (); }
  std::string get_zminorgrid (void) const { return zminorgrid.current_value (); }

  octave_value get_xtick (void) const { return xtick.get (); }

  octave_value get_ytick (void) const { return ytick.get (); }

  octave_value get_ztick (void) const { return ztick.get (); }

  bool xtickmode_is (const std::string& v) const { return xtickmode.is (v); }
  std::string get_xtickmode (void) const { return xtickmode.current_value (); }

  bool ytickmode_is (const std::string& v) const { return ytickmode.is (v); }
  std::string get_ytickmode (void) const { return ytickmode.current_value (); }

  bool ztickmode_is (const std::string& v) const { return ztickmode.is (v); }
  std::string get_ztickmode (void) const { return ztickmode.current_value (); }

  bool is_xminortick (void) const { return xminortick.is_on (); }
  std::string get_xminortick (void) const { return xminortick.current_value (); }

  bool is_yminortick (void) const { return yminortick.is_on (); }
  std::string get_yminortick (void) const { return yminortick.current_value (); }

  bool is_zminortick (void) const { return zminortick.is_on (); }
  std::string get_zminortick (void) const { return zminortick.current_value (); }

  octave_value get_xticklabel (void) const { return xticklabel.get (); }

  octave_value get_yticklabel (void) const { return yticklabel.get (); }

  octave_value get_zticklabel (void) const { return zticklabel.get (); }

  bool xticklabelmode_is (const std::string& v) const { return xticklabelmode.is (v); }
  std::string get_xticklabelmode (void) const { return xticklabelmode.current_value (); }

  bool yticklabelmode_is (const std::string& v) const { return yticklabelmode.is (v); }
  std::string get_yticklabelmode (void) const { return yticklabelmode.current_value (); }

  bool zticklabelmode_is (const std::string& v) const { return zticklabelmode.is (v); }
  std::string get_zticklabelmode (void) const { return zticklabelmode.current_value (); }

  bool interpreter_is (const std::string& v) const { return interpreter.is (v); }
  std::string get_interpreter (void) const { return interpreter.current_value (); }

  bool color_is_rgb (void) const { return color.is_rgb (); }
  bool color_is (const std::string& v) const { return color.is (v); }
  Matrix get_color_rgb (void) const { return (color.is_rgb () ? color.rgb () : Matrix ()); }
  octave_value get_color (void) const { return color.get (); }

  bool xcolor_is_rgb (void) const { return xcolor.is_rgb (); }
  bool xcolor_is (const std::string& v) const { return xcolor.is (v); }
  Matrix get_xcolor_rgb (void) const { return (xcolor.is_rgb () ? xcolor.rgb () : Matrix ()); }
  octave_value get_xcolor (void) const { return xcolor.get (); }

  bool ycolor_is_rgb (void) const { return ycolor.is_rgb (); }
  bool ycolor_is (const std::string& v) const { return ycolor.is (v); }
  Matrix get_ycolor_rgb (void) const { return (ycolor.is_rgb () ? ycolor.rgb () : Matrix ()); }
  octave_value get_ycolor (void) const { return ycolor.get (); }

  bool zcolor_is_rgb (void) const { return zcolor.is_rgb (); }
  bool zcolor_is (const std::string& v) const { return zcolor.is (v); }
  Matrix get_zcolor_rgb (void) const { return (zcolor.is_rgb () ? zcolor.rgb () : Matrix ()); }
  octave_value get_zcolor (void) const { return zcolor.get (); }

  bool xscale_is (const std::string& v) const { return xscale.is (v); }
  std::string get_xscale (void) const { return xscale.current_value (); }

  bool yscale_is (const std::string& v) const { return yscale.is (v); }
  std::string get_yscale (void) const { return yscale.current_value (); }

  bool zscale_is (const std::string& v) const { return zscale.is (v); }
  std::string get_zscale (void) const { return zscale.current_value (); }

  bool xdir_is (const std::string& v) const { return xdir.is (v); }
  std::string get_xdir (void) const { return xdir.current_value (); }

  bool ydir_is (const std::string& v) const { return ydir.is (v); }
  std::string get_ydir (void) const { return ydir.current_value (); }

  bool zdir_is (const std::string& v) const { return zdir.is (v); }
  std::string get_zdir (void) const { return zdir.current_value (); }

  bool yaxislocation_is (const std::string& v) const { return yaxislocation.is (v); }
  std::string get_yaxislocation (void) const { return yaxislocation.current_value (); }

  bool xaxislocation_is (const std::string& v) const { return xaxislocation.is (v); }
  std::string get_xaxislocation (void) const { return xaxislocation.current_value (); }

  octave_value get_view (void) const { return view.get (); }

  bool nextplot_is (const std::string& v) const { return nextplot.is (v); }
  std::string get_nextplot (void) const { return nextplot.current_value (); }

  octave_value get_outerposition (void) const { return outerposition.get (); }

  bool activepositionproperty_is (const std::string& v) const { return activepositionproperty.is (v); }
  std::string get_activepositionproperty (void) const { return activepositionproperty.current_value (); }

  bool ambientlightcolor_is_rgb (void) const { return ambientlightcolor.is_rgb (); }
  bool ambientlightcolor_is (const std::string& v) const { return ambientlightcolor.is (v); }
  Matrix get_ambientlightcolor_rgb (void) const { return (ambientlightcolor.is_rgb () ? ambientlightcolor.rgb () : Matrix ()); }
  octave_value get_ambientlightcolor (void) const { return ambientlightcolor.get (); }

  octave_value get_cameraposition (void) const { return cameraposition.get (); }

  octave_value get_cameratarget (void) const { return cameratarget.get (); }

  octave_value get_cameraupvector (void) const { return cameraupvector.get (); }

  double get_cameraviewangle (void) const { return cameraviewangle.double_value (); }

  bool camerapositionmode_is (const std::string& v) const { return camerapositionmode.is (v); }
  std::string get_camerapositionmode (void) const { return camerapositionmode.current_value (); }

  bool cameratargetmode_is (const std::string& v) const { return cameratargetmode.is (v); }
  std::string get_cameratargetmode (void) const { return cameratargetmode.current_value (); }

  bool cameraupvectormode_is (const std::string& v) const { return cameraupvectormode.is (v); }
  std::string get_cameraupvectormode (void) const { return cameraupvectormode.current_value (); }

  bool cameraviewanglemode_is (const std::string& v) const { return cameraviewanglemode.is (v); }
  std::string get_cameraviewanglemode (void) const { return cameraviewanglemode.current_value (); }

  octave_value get_currentpoint (void) const { return currentpoint.get (); }

  bool drawmode_is (const std::string& v) const { return drawmode.is (v); }
  std::string get_drawmode (void) const { return drawmode.current_value (); }

  bool fontangle_is (const std::string& v) const { return fontangle.is (v); }
  std::string get_fontangle (void) const { return fontangle.current_value (); }

  std::string get_fontname (void) const { return fontname.string_value (); }

  double get_fontsize (void) const { return fontsize.double_value (); }

  bool fontunits_is (const std::string& v) const { return fontunits.is (v); }
  std::string get_fontunits (void) const { return fontunits.current_value (); }

  bool fontweight_is (const std::string& v) const { return fontweight.is (v); }
  std::string get_fontweight (void) const { return fontweight.current_value (); }

  bool gridlinestyle_is (const std::string& v) const { return gridlinestyle.is (v); }
  std::string get_gridlinestyle (void) const { return gridlinestyle.current_value (); }

  std::string get_linestyleorder (void) const { return linestyleorder.string_value (); }

  double get_linewidth (void) const { return linewidth.double_value (); }

  bool minorgridlinestyle_is (const std::string& v) const { return minorgridlinestyle.is (v); }
  std::string get_minorgridlinestyle (void) const { return minorgridlinestyle.current_value (); }

  octave_value get_plotboxaspectratio (void) const { return plotboxaspectratio.get (); }

  bool plotboxaspectratiomode_is (const std::string& v) const { return plotboxaspectratiomode.is (v); }
  std::string get_plotboxaspectratiomode (void) const { return plotboxaspectratiomode.current_value (); }

  bool projection_is (const std::string& v) const { return projection.is (v); }
  std::string get_projection (void) const { return projection.current_value (); }

  bool tickdir_is (const std::string& v) const { return tickdir.is (v); }
  std::string get_tickdir (void) const { return tickdir.current_value (); }

  bool tickdirmode_is (const std::string& v) const { return tickdirmode.is (v); }
  std::string get_tickdirmode (void) const { return tickdirmode.current_value (); }

  octave_value get_ticklength (void) const { return ticklength.get (); }

  octave_value get_tightinset (void) const { return tightinset.get (); }

  bool units_is (const std::string& v) const { return units.is (v); }
  std::string get_units (void) const { return units.current_value (); }

  octave_value get_x_viewtransform (void) const { return x_viewtransform.get (); }

  octave_value get_x_projectiontransform (void) const { return x_projectiontransform.get (); }

  octave_value get_x_viewporttransform (void) const { return x_viewporttransform.get (); }

  octave_value get_x_normrendertransform (void) const { return x_normrendertransform.get (); }

  octave_value get_x_rendertransform (void) const { return x_rendertransform.get (); }


  void set_position (const octave_value& val)
  {
    if (! error_state)
      {
        if (position.set (val, true))
          {
            update_position ();
            mark_modified ();
          }
      }
  }

  void set_box (const octave_value& val)
  {
    if (! error_state)
      {
        if (box.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_key (const octave_value& val)
  {
    if (! error_state)
      {
        if (key.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keybox (const octave_value& val)
  {
    if (! error_state)
      {
        if (keybox.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keyreverse (const octave_value& val)
  {
    if (! error_state)
      {
        if (keyreverse.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keypos (const octave_value& val)
  {
    if (! error_state)
      {
        if (keypos.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_colororder (const octave_value& val)
  {
    if (! error_state)
      {
        if (colororder.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_dataaspectratio (const octave_value& val)
  {
    if (! error_state)
      {
        if (dataaspectratio.set (val, false))
          {
            set_dataaspectratiomode ("manual");
            dataaspectratio.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_dataaspectratiomode ("manual");
      }
  }

  void set_dataaspectratiomode (const octave_value& val)
  {
    if (! error_state)
      {
        if (dataaspectratiomode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_layer (const octave_value& val)
  {
    if (! error_state)
      {
        if (layer.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            set_xlimmode ("manual");
            update_xlim ();
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_xlimmode ("manual");
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            set_ylimmode ("manual");
            update_ylim ();
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_ylimmode ("manual");
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, false))
          {
            set_zlimmode ("manual");
            update_zlim ();
            zlim.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_zlimmode ("manual");
      }
  }

  void set_clim (const octave_value& val)
  {
    if (! error_state)
      {
        if (clim.set (val, false))
          {
            set_climmode ("manual");
            clim.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_climmode ("manual");
      }
  }

  void set_alim (const octave_value& val)
  {
    if (! error_state)
      {
        if (alim.set (val, false))
          {
            set_alimmode ("manual");
            alim.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_alimmode ("manual");
      }
  }

  void set_xlimmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlimmode.set (val, false))
          {
            update_axis_limits ("xlimmode");
            xlimmode.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylimmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylimmode.set (val, false))
          {
            update_axis_limits ("ylimmode");
            ylimmode.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zlimmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlimmode.set (val, false))
          {
            update_axis_limits ("zlimmode");
            zlimmode.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_climmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (climmode.set (val, false))
          {
            update_axis_limits ("climmode");
            climmode.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_alimmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (alimmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xlabel (const octave_value& val);

  void set_ylabel (const octave_value& val);

  void set_zlabel (const octave_value& val);

  void set_title (const octave_value& val);

  void set_xgrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (xgrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ygrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (ygrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zgrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (zgrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xminorgrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (xminorgrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_yminorgrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (yminorgrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zminorgrid (const octave_value& val)
  {
    if (! error_state)
      {
        if (zminorgrid.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xtick (const octave_value& val)
  {
    if (! error_state)
      {
        if (xtick.set (val, false))
          {
            set_xtickmode ("manual");
            xtick.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_xtickmode ("manual");
      }
  }

  void set_ytick (const octave_value& val)
  {
    if (! error_state)
      {
        if (ytick.set (val, false))
          {
            set_ytickmode ("manual");
            ytick.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_ytickmode ("manual");
      }
  }

  void set_ztick (const octave_value& val)
  {
    if (! error_state)
      {
        if (ztick.set (val, false))
          {
            set_ztickmode ("manual");
            ztick.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_ztickmode ("manual");
      }
  }

  void set_xtickmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (xtickmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ytickmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (ytickmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ztickmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (ztickmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xminortick (const octave_value& val)
  {
    if (! error_state)
      {
        if (xminortick.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_yminortick (const octave_value& val)
  {
    if (! error_state)
      {
        if (yminortick.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zminortick (const octave_value& val)
  {
    if (! error_state)
      {
        if (zminortick.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xticklabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (xticklabel.set (val, false))
          {
            set_xticklabelmode ("manual");
            xticklabel.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_xticklabelmode ("manual");
      }
  }

  void set_yticklabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (yticklabel.set (val, false))
          {
            set_yticklabelmode ("manual");
            yticklabel.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_yticklabelmode ("manual");
      }
  }

  void set_zticklabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (zticklabel.set (val, false))
          {
            set_zticklabelmode ("manual");
            zticklabel.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_zticklabelmode ("manual");
      }
  }

  void set_xticklabelmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (xticklabelmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_yticklabelmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (yticklabelmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zticklabelmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (zticklabelmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interpreter (const octave_value& val)
  {
    if (! error_state)
      {
        if (interpreter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_color (const octave_value& val)
  {
    if (! error_state)
      {
        if (color.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xcolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (xcolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ycolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (ycolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zcolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (zcolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xscale (const octave_value& val)
  {
    if (! error_state)
      {
        if (xscale.set (val, false))
          {
            update_xscale ();
            update_axis_limits ("xscale");
            xscale.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yscale (const octave_value& val)
  {
    if (! error_state)
      {
        if (yscale.set (val, false))
          {
            update_yscale ();
            update_axis_limits ("yscale");
            yscale.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zscale (const octave_value& val)
  {
    if (! error_state)
      {
        if (zscale.set (val, false))
          {
            update_zscale ();
            update_axis_limits ("zscale");
            zscale.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xdir (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdir.set (val, true))
          {
            update_xdir ();
            mark_modified ();
          }
      }
  }

  void set_ydir (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydir.set (val, true))
          {
            update_ydir ();
            mark_modified ();
          }
      }
  }

  void set_zdir (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdir.set (val, true))
          {
            update_zdir ();
            mark_modified ();
          }
      }
  }

  void set_yaxislocation (const octave_value& val)
  {
    if (! error_state)
      {
        if (yaxislocation.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xaxislocation (const octave_value& val)
  {
    if (! error_state)
      {
        if (xaxislocation.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_view (const octave_value& val)
  {
    if (! error_state)
      {
        if (view.set (val, true))
          {
            update_view ();
            mark_modified ();
          }
      }
  }

  void set_nextplot (const octave_value& val)
  {
    if (! error_state)
      {
        if (nextplot.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_outerposition (const octave_value& val)
  {
    if (! error_state)
      {
        if (outerposition.set (val, true))
          {
            update_outerposition ();
            mark_modified ();
          }
      }
  }

  void set_activepositionproperty (const octave_value& val)
  {
    if (! error_state)
      {
        if (activepositionproperty.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ambientlightcolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (ambientlightcolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_cameraposition (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameraposition.set (val, false))
          {
            set_camerapositionmode ("manual");
            cameraposition.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_camerapositionmode ("manual");
      }
  }

  void set_cameratarget (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameratarget.set (val, false))
          {
            set_cameratargetmode ("manual");
            cameratarget.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_cameratargetmode ("manual");
      }
  }

  void set_cameraupvector (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameraupvector.set (val, false))
          {
            set_cameraupvectormode ("manual");
            cameraupvector.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_cameraupvectormode ("manual");
      }
  }

  void set_cameraviewangle (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameraviewangle.set (val, false))
          {
            set_cameraviewanglemode ("manual");
            cameraviewangle.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_cameraviewanglemode ("manual");
      }
  }

  void set_camerapositionmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (camerapositionmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_cameratargetmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameratargetmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_cameraupvectormode (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameraupvectormode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_cameraviewanglemode (const octave_value& val)
  {
    if (! error_state)
      {
        if (cameraviewanglemode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_currentpoint (const octave_value& val)
  {
    if (! error_state)
      {
        if (currentpoint.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_drawmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (drawmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontangle (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontangle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontname (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontname.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontsize (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontsize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontunits (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontunits.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontweight (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontweight.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_gridlinestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (gridlinestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linestyleorder (const octave_value& val)
  {
    if (! error_state)
      {
        if (linestyleorder.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linewidth (const octave_value& val)
  {
    if (! error_state)
      {
        if (linewidth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_minorgridlinestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (minorgridlinestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_plotboxaspectratio (const octave_value& val)
  {
    if (! error_state)
      {
        if (plotboxaspectratio.set (val, false))
          {
            set_plotboxaspectratiomode ("manual");
            plotboxaspectratio.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_plotboxaspectratiomode ("manual");
      }
  }

  void set_plotboxaspectratiomode (const octave_value& val)
  {
    if (! error_state)
      {
        if (plotboxaspectratiomode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_projection (const octave_value& val)
  {
    if (! error_state)
      {
        if (projection.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_tickdir (const octave_value& val)
  {
    if (! error_state)
      {
        if (tickdir.set (val, false))
          {
            set_tickdirmode ("manual");
            tickdir.run_listeners (POSTSET);
            mark_modified ();
          }
        else
          set_tickdirmode ("manual");
      }
  }

  void set_tickdirmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (tickdirmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ticklength (const octave_value& val)
  {
    if (! error_state)
      {
        if (ticklength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_tightinset (const octave_value& val)
  {
    if (! error_state)
      {
        if (tightinset.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_units (const octave_value& val)
  {
    if (! error_state)
      {
        if (units.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_x_viewtransform (const octave_value& val)
  {
    if (! error_state)
      {
        if (x_viewtransform.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_x_projectiontransform (const octave_value& val)
  {
    if (! error_state)
      {
        if (x_projectiontransform.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_x_viewporttransform (const octave_value& val)
  {
    if (! error_state)
      {
        if (x_viewporttransform.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_x_normrendertransform (const octave_value& val)
  {
    if (! error_state)
      {
        if (x_normrendertransform.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_x_rendertransform (const octave_value& val)
  {
    if (! error_state)
      {
        if (x_rendertransform.set (val, true))
          {
            mark_modified ();
          }
      }
  }


  protected:
    void init (void);

  private:
    void update_xscale (void) { sx = get_xscale (); }
    void update_yscale (void) { sy = get_yscale (); }
    void update_zscale (void) { sz = get_zscale (); }

    void update_view (void) { update_camera (); }

    void update_xdir (void) { update_camera (); }
    void update_ydir (void) { update_camera (); }
    void update_zdir (void) { update_camera (); }

    void sync_positions (void);
    void update_outerposition (void) { sync_positions ();}
    void update_position (void) { sync_positions (); }

    double calc_tick_sep (double minval, double maxval);
    void calc_ticks_and_lims (array_property& lims, array_property& ticks, bool limmode_is_auto, bool is_logscale);
    void fix_limits (array_property& lims)
    {
      if (lims.get ().is_empty ()) 
	return;

      Matrix l = lims.get ().matrix_value ();
      if (l(0) > l(1))
	{
	  l(0) = 0;
	  l(1) = 1;
	  lims = l;
	}
      else if (l(0) == l(1))
	{
	  l(0) -= 0.5;
	  l(1) += 0.5;
	  lims = l;
	}
    }      

  public:
    Matrix get_axis_limits (double xmin, double xmax, double min_pos, bool logscale);
    
    void update_xlim (bool do_clr_zoom = true)
    {
      if (xtickmode.is ("auto"))
	calc_ticks_and_lims (xlim, xtick, xlimmode.is ("auto"), xscale.is ("log"));

      fix_limits (xlim);

      if (do_clr_zoom)
	zoom_stack.clear ();
    }

    void update_ylim (bool do_clr_zoom = true)
    {
      if (ytickmode.is ("auto"))
	calc_ticks_and_lims (ylim, ytick, ylimmode.is ("auto"), yscale.is ("log"));

      fix_limits (ylim);

      if (do_clr_zoom)
	zoom_stack.clear ();
    }

    void update_zlim (void)
    {
      if (ztickmode.is ("auto"))
	calc_ticks_and_lims (zlim, ztick, zlimmode.is ("auto"), zscale.is ("log"));

      fix_limits (zlim);

      zoom_stack.clear ();
    }
    
  };

private:
  properties xproperties;

public:
  axes (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p), default_properties ()
  {
    xproperties.override_defaults (*this);
    xproperties.update_transform ();
  }

  ~axes (void) { xproperties.delete_children (); }

  void override_defaults (base_graphics_object& obj)
  {
    // Allow parent (figure) to override first (properties knows how
    // to find the parent object).
    xproperties.override_defaults (obj);

    // Now override with our defaults.  If the default_properties
    // list includes the properties for all defaults (line,
    // surface, etc.) then we don't have to know the type of OBJ
    // here, we just call its set function and let it decide which
    // properties from the list to use.
    obj.set_from_list (default_properties);
  }

  void set (const caseless_str& name, const octave_value& value)
  {
    if (name.compare ("default", 7))
      // strip "default", pass rest to function that will
      // parse the remainder and add the element to the
      // default_properties map.
      default_properties.set (name.substr (7), value);
    else
      xproperties.set (name, value);
  }

  void set_defaults (const std::string& mode)
  {
    remove_all_listeners ();
    xproperties.set_defaults (*this, mode);
  }

  octave_value get (const caseless_str& name) const
  {
    octave_value retval;

    // FIXME -- finish this.
    if (name.compare ("default", 7))
      retval = get_default (name.substr (7));
    else
      retval = xproperties.get (name);

    return retval;
  }

  octave_value get_default (const caseless_str& name) const;

  octave_value get_defaults (void) const
  {
    return default_properties.as_struct ("default");
  }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  void update_axis_limits (const std::string& axis_type);

  bool valid_object (void) const { return true; }

private:
  property_list default_properties;
};

// ---------------------------------------------------------------------

class OCTINTERP_API line : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    // See the genprops.awk script for an explanation of the
    // properties declarations.

    // properties which are not in matlab:
    // ldata, udata, xldata, xudata, keylabel, interpreter

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  row_vector_property xdata;
  row_vector_property ydata;
  row_vector_property zdata;
  row_vector_property ldata;
  row_vector_property udata;
  row_vector_property xldata;
  row_vector_property xudata;
  string_property xdatasource;
  string_property ydatasource;
  string_property zdatasource;
  color_property color;
  radio_property linestyle;
  double_property linewidth;
  radio_property marker;
  color_property markeredgecolor;
  color_property markerfacecolor;
  double_property markersize;
  string_property keylabel;
  radio_property interpreter;
  string_property displayname;
  radio_property erasemode;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property zliminclude;

public:

  enum
  {
    XDATA = 4000,
    YDATA = 4001,
    ZDATA = 4002,
    LDATA = 4003,
    UDATA = 4004,
    XLDATA = 4005,
    XUDATA = 4006,
    XDATASOURCE = 4007,
    YDATASOURCE = 4008,
    ZDATASOURCE = 4009,
    COLOR = 4010,
    LINESTYLE = 4011,
    LINEWIDTH = 4012,
    MARKER = 4013,
    MARKEREDGECOLOR = 4014,
    MARKERFACECOLOR = 4015,
    MARKERSIZE = 4016,
    KEYLABEL = 4017,
    INTERPRETER = 4018,
    DISPLAYNAME = 4019,
    ERASEMODE = 4020,
    XLIM = 4021,
    YLIM = 4022,
    ZLIM = 4023,
    XLIMINCLUDE = 4024,
    YLIMINCLUDE = 4025,
    ZLIMINCLUDE = 4026
  };

  octave_value get_xdata (void) const { return xdata.get (); }

  octave_value get_ydata (void) const { return ydata.get (); }

  octave_value get_zdata (void) const { return zdata.get (); }

  octave_value get_ldata (void) const { return ldata.get (); }

  octave_value get_udata (void) const { return udata.get (); }

  octave_value get_xldata (void) const { return xldata.get (); }

  octave_value get_xudata (void) const { return xudata.get (); }

  std::string get_xdatasource (void) const { return xdatasource.string_value (); }

  std::string get_ydatasource (void) const { return ydatasource.string_value (); }

  std::string get_zdatasource (void) const { return zdatasource.string_value (); }

  bool color_is_rgb (void) const { return color.is_rgb (); }
  bool color_is (const std::string& v) const { return color.is (v); }
  Matrix get_color_rgb (void) const { return (color.is_rgb () ? color.rgb () : Matrix ()); }
  octave_value get_color (void) const { return color.get (); }

  bool linestyle_is (const std::string& v) const { return linestyle.is (v); }
  std::string get_linestyle (void) const { return linestyle.current_value (); }

  double get_linewidth (void) const { return linewidth.double_value (); }

  bool marker_is (const std::string& v) const { return marker.is (v); }
  std::string get_marker (void) const { return marker.current_value (); }

  bool markeredgecolor_is_rgb (void) const { return markeredgecolor.is_rgb (); }
  bool markeredgecolor_is (const std::string& v) const { return markeredgecolor.is (v); }
  Matrix get_markeredgecolor_rgb (void) const { return (markeredgecolor.is_rgb () ? markeredgecolor.rgb () : Matrix ()); }
  octave_value get_markeredgecolor (void) const { return markeredgecolor.get (); }

  bool markerfacecolor_is_rgb (void) const { return markerfacecolor.is_rgb (); }
  bool markerfacecolor_is (const std::string& v) const { return markerfacecolor.is (v); }
  Matrix get_markerfacecolor_rgb (void) const { return (markerfacecolor.is_rgb () ? markerfacecolor.rgb () : Matrix ()); }
  octave_value get_markerfacecolor (void) const { return markerfacecolor.get (); }

  double get_markersize (void) const { return markersize.double_value (); }

  std::string get_keylabel (void) const { return keylabel.string_value (); }

  bool interpreter_is (const std::string& v) const { return interpreter.is (v); }
  std::string get_interpreter (void) const { return interpreter.current_value (); }

  std::string get_displayname (void) const { return displayname.string_value (); }

  bool erasemode_is (const std::string& v) const { return erasemode.is (v); }
  std::string get_erasemode (void) const { return erasemode.current_value (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }

  bool is_zliminclude (void) const { return zliminclude.is_on (); }
  std::string get_zliminclude (void) const { return zliminclude.current_value (); }


  void set_xdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdata.set (val, true))
          {
            update_xdata ();
            mark_modified ();
          }
      }
  }

  void set_ydata (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydata.set (val, true))
          {
            update_ydata ();
            mark_modified ();
          }
      }
  }

  void set_zdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdata.set (val, true))
          {
            update_zdata ();
            mark_modified ();
          }
      }
  }

  void set_ldata (const octave_value& val)
  {
    if (! error_state)
      {
        if (ldata.set (val, true))
          {
            update_ldata ();
            mark_modified ();
          }
      }
  }

  void set_udata (const octave_value& val)
  {
    if (! error_state)
      {
        if (udata.set (val, true))
          {
            update_udata ();
            mark_modified ();
          }
      }
  }

  void set_xldata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xldata.set (val, true))
          {
            update_xldata ();
            mark_modified ();
          }
      }
  }

  void set_xudata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xudata.set (val, true))
          {
            update_xudata ();
            mark_modified ();
          }
      }
  }

  void set_xdatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ydatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zdatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_color (const octave_value& val)
  {
    if (! error_state)
      {
        if (color.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (linestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linewidth (const octave_value& val)
  {
    if (! error_state)
      {
        if (linewidth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_marker (const octave_value& val)
  {
    if (! error_state)
      {
        if (marker.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markeredgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markeredgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markerfacecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markerfacecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markersize (const octave_value& val)
  {
    if (! error_state)
      {
        if (markersize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keylabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (keylabel.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interpreter (const octave_value& val)
  {
    if (! error_state)
      {
        if (interpreter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_displayname (const octave_value& val)
  {
    if (! error_state)
      {
        if (displayname.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_erasemode (const octave_value& val)
  {
    if (! error_state)
      {
        if (erasemode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            update_axis_limits ("xlim");
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            update_axis_limits ("ylim");
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, false))
          {
            update_axis_limits ("zlim");
            zlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, false))
          {
            update_axis_limits ("xliminclude");
            xliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, false))
          {
            update_axis_limits ("yliminclude");
            yliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (zliminclude.set (val, false))
          {
            update_axis_limits ("zliminclude");
            zliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }


  private:
    Matrix compute_xlim (void) const;
    Matrix compute_ylim (void) const;

    void update_xdata (void) { set_xlim (compute_xlim ()); }
    void update_xldata (void) { set_xlim (compute_xlim ()); }
    void update_xudata (void) { set_xlim (compute_xlim ()); }
    
    void update_ydata (void) { set_ylim (compute_ylim ()); }
    void update_ldata (void) { set_ylim (compute_ylim ()); }
    void update_udata (void) { set_ylim (compute_ylim ()); }

    void update_zdata (void)
      {
	set_zlim (zdata.get_limits ());
	set_zliminclude (get_zdata ().numel () > 0);
      }
  };

private:
  properties xproperties;

public:
  line (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~line (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
};

// ---------------------------------------------------------------------

class OCTINTERP_API text : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  string_property string;
  radio_property units;
  array_property position;
  double_property rotation;
  radio_property horizontalalignment;
  color_property color;
  string_property fontname;
  double_property fontsize;
  radio_property fontangle;
  radio_property fontweight;
  radio_property interpreter;
  color_property backgroundcolor;
  string_property displayname;
  color_property edgecolor;
  radio_property erasemode;
  bool_property editing;
  radio_property fontunits;
  radio_property linestyle;
  double_property linewidth;
  double_property margin;
  radio_property verticalalignment;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property zliminclude;

public:

  enum
  {
    STRING = 5000,
    UNITS = 5001,
    POSITION = 5002,
    ROTATION = 5003,
    HORIZONTALALIGNMENT = 5004,
    COLOR = 5005,
    FONTNAME = 5006,
    FONTSIZE = 5007,
    FONTANGLE = 5008,
    FONTWEIGHT = 5009,
    INTERPRETER = 5010,
    BACKGROUNDCOLOR = 5011,
    DISPLAYNAME = 5012,
    EDGECOLOR = 5013,
    ERASEMODE = 5014,
    EDITING = 5015,
    FONTUNITS = 5016,
    LINESTYLE = 5017,
    LINEWIDTH = 5018,
    MARGIN = 5019,
    VERTICALALIGNMENT = 5020,
    XLIM = 5021,
    YLIM = 5022,
    ZLIM = 5023,
    XLIMINCLUDE = 5024,
    YLIMINCLUDE = 5025,
    ZLIMINCLUDE = 5026
  };

  std::string get_string (void) const { return string.string_value (); }

  bool units_is (const std::string& v) const { return units.is (v); }
  std::string get_units (void) const { return units.current_value (); }

  octave_value get_position (void) const { return position.get (); }

  double get_rotation (void) const { return rotation.double_value (); }

  bool horizontalalignment_is (const std::string& v) const { return horizontalalignment.is (v); }
  std::string get_horizontalalignment (void) const { return horizontalalignment.current_value (); }

  bool color_is_rgb (void) const { return color.is_rgb (); }
  bool color_is (const std::string& v) const { return color.is (v); }
  Matrix get_color_rgb (void) const { return (color.is_rgb () ? color.rgb () : Matrix ()); }
  octave_value get_color (void) const { return color.get (); }

  std::string get_fontname (void) const { return fontname.string_value (); }

  double get_fontsize (void) const { return fontsize.double_value (); }

  bool fontangle_is (const std::string& v) const { return fontangle.is (v); }
  std::string get_fontangle (void) const { return fontangle.current_value (); }

  bool fontweight_is (const std::string& v) const { return fontweight.is (v); }
  std::string get_fontweight (void) const { return fontweight.current_value (); }

  bool interpreter_is (const std::string& v) const { return interpreter.is (v); }
  std::string get_interpreter (void) const { return interpreter.current_value (); }

  bool backgroundcolor_is_rgb (void) const { return backgroundcolor.is_rgb (); }
  bool backgroundcolor_is (const std::string& v) const { return backgroundcolor.is (v); }
  Matrix get_backgroundcolor_rgb (void) const { return (backgroundcolor.is_rgb () ? backgroundcolor.rgb () : Matrix ()); }
  octave_value get_backgroundcolor (void) const { return backgroundcolor.get (); }

  std::string get_displayname (void) const { return displayname.string_value (); }

  bool edgecolor_is_rgb (void) const { return edgecolor.is_rgb (); }
  bool edgecolor_is (const std::string& v) const { return edgecolor.is (v); }
  Matrix get_edgecolor_rgb (void) const { return (edgecolor.is_rgb () ? edgecolor.rgb () : Matrix ()); }
  octave_value get_edgecolor (void) const { return edgecolor.get (); }

  bool erasemode_is (const std::string& v) const { return erasemode.is (v); }
  std::string get_erasemode (void) const { return erasemode.current_value (); }

  bool is_editing (void) const { return editing.is_on (); }
  std::string get_editing (void) const { return editing.current_value (); }

  bool fontunits_is (const std::string& v) const { return fontunits.is (v); }
  std::string get_fontunits (void) const { return fontunits.current_value (); }

  bool linestyle_is (const std::string& v) const { return linestyle.is (v); }
  std::string get_linestyle (void) const { return linestyle.current_value (); }

  double get_linewidth (void) const { return linewidth.double_value (); }

  double get_margin (void) const { return margin.double_value (); }

  bool verticalalignment_is (const std::string& v) const { return verticalalignment.is (v); }
  std::string get_verticalalignment (void) const { return verticalalignment.current_value (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }

  bool is_zliminclude (void) const { return zliminclude.is_on (); }
  std::string get_zliminclude (void) const { return zliminclude.current_value (); }


  void set_string (const octave_value& val)
  {
    if (! error_state)
      {
        if (string.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_units (const octave_value& val)
  {
    if (! error_state)
      {
        if (units.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_position (const octave_value& val)
  {
    if (! error_state)
      {
        if (position.set (val, true))
          {
            update_position ();
            mark_modified ();
          }
      }
  }

  void set_rotation (const octave_value& val)
  {
    if (! error_state)
      {
        if (rotation.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_horizontalalignment (const octave_value& val)
  {
    if (! error_state)
      {
        if (horizontalalignment.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_color (const octave_value& val)
  {
    if (! error_state)
      {
        if (color.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontname (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontname.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontsize (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontsize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontangle (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontangle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontweight (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontweight.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interpreter (const octave_value& val)
  {
    if (! error_state)
      {
        if (interpreter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_backgroundcolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (backgroundcolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_displayname (const octave_value& val)
  {
    if (! error_state)
      {
        if (displayname.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_erasemode (const octave_value& val)
  {
    if (! error_state)
      {
        if (erasemode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_editing (const octave_value& val)
  {
    if (! error_state)
      {
        if (editing.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_fontunits (const octave_value& val)
  {
    if (! error_state)
      {
        if (fontunits.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (linestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linewidth (const octave_value& val)
  {
    if (! error_state)
      {
        if (linewidth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_margin (const octave_value& val)
  {
    if (! error_state)
      {
        if (margin.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_verticalalignment (const octave_value& val)
  {
    if (! error_state)
      {
        if (verticalalignment.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            update_axis_limits ("xlim");
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            update_axis_limits ("ylim");
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, false))
          {
            update_axis_limits ("zlim");
            zlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, false))
          {
            update_axis_limits ("xliminclude");
            xliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, false))
          {
            update_axis_limits ("yliminclude");
            yliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (zliminclude.set (val, false))
          {
            update_axis_limits ("zliminclude");
            zliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }


  protected:
    void init (void)
      {
        position.add_constraint (dim_vector (1, 3));
      }

  private:
    void update_position (void)
      {
	Matrix pos = get_position ().matrix_value ();
	Matrix lim;

	lim = Matrix (1, 3, pos(0));
	lim(2) = (lim(2) <= 0 ? octave_Inf : lim(2));
	set_xlim (lim);

	lim = Matrix (1, 3, pos(1));
	lim(2) = (lim(2) <= 0 ? octave_Inf : lim(2));
	set_ylim (lim);

	if (pos.numel () == 3)
	  {
	    lim = Matrix (1, 3, pos(2));
	    lim(2) = (lim(2) <= 0 ? octave_Inf : lim(2));
	    set_zliminclude ("on");
	    set_zlim (lim);
	  }
	else
	  set_zliminclude ("off");
      }
  };

private:
  properties xproperties;

public:
  text (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~text (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
};

// ---------------------------------------------------------------------

class OCTINTERP_API image : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    bool is_climinclude (void) const
      { return (climinclude.is_on () && cdatamapping.is ("scaled")); }
    std::string get_climinclude (void) const
      { return climinclude.current_value (); }

    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  row_vector_property xdata;
  row_vector_property ydata;
  array_property cdata;
  radio_property cdatamapping;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property clim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property climinclude;

public:

  enum
  {
    XDATA = 6000,
    YDATA = 6001,
    CDATA = 6002,
    CDATAMAPPING = 6003,
    XLIM = 6004,
    YLIM = 6005,
    CLIM = 6006,
    XLIMINCLUDE = 6007,
    YLIMINCLUDE = 6008,
    CLIMINCLUDE = 6009
  };

  octave_value get_xdata (void) const { return xdata.get (); }

  octave_value get_ydata (void) const { return ydata.get (); }

  octave_value get_cdata (void) const { return cdata.get (); }

  bool cdatamapping_is (const std::string& v) const { return cdatamapping.is (v); }
  std::string get_cdatamapping (void) const { return cdatamapping.current_value (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_clim (void) const { return clim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }


  void set_xdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdata.set (val, true))
          {
            update_xdata ();
            mark_modified ();
          }
      }
  }

  void set_ydata (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydata.set (val, true))
          {
            update_ydata ();
            mark_modified ();
          }
      }
  }

  void set_cdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdata.set (val, true))
          {
            update_cdata ();
            mark_modified ();
          }
      }
  }

  void set_cdatamapping (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdatamapping.set (val, false))
          {
            update_axis_limits ("cdatamapping");
            cdatamapping.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            update_axis_limits ("xlim");
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            update_axis_limits ("ylim");
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_clim (const octave_value& val)
  {
    if (! error_state)
      {
        if (clim.set (val, false))
          {
            update_axis_limits ("clim");
            clim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, false))
          {
            update_axis_limits ("xliminclude");
            xliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, false))
          {
            update_axis_limits ("yliminclude");
            yliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_climinclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (climinclude.set (val, false))
          {
            update_axis_limits ("climinclude");
            climinclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }


  protected:
    void init (void)
      {
	xdata.add_constraint (2);
	ydata.add_constraint (2);
	cdata.add_constraint ("double");
	cdata.add_constraint ("logical");
	cdata.add_constraint ("uint8");
	cdata.add_constraint (dim_vector (-1, -1));
	cdata.add_constraint (dim_vector (-1, -1, 3));
      }

  private:
    // FIXME -- limits should take pixel width into account.
    void update_xdata (void)
      { set_xlim (xdata.get_limits ()); }

    // FIXME -- idem.
    void update_ydata (void)
      { set_ylim (ydata.get_limits ()); }

    void update_cdata (void)
      {
	if (cdatamapping_is ("scaled"))
	  set_clim (cdata.get_limits ());
	else
	  clim = cdata.get_limits ();
      }
  };

private:
  properties xproperties;

public:
  image (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~image (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
};

// ---------------------------------------------------------------------

class OCTINTERP_API patch : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    octave_value get_color_data (void) const;
    
    bool is_climinclude (void) const
      { return (climinclude.is_on () && cdatamapping.is ("scaled")); }
    std::string get_climinclude (void) const
      { return climinclude.current_value (); }

    bool is_aliminclude (void) const
      { return (aliminclude.is_on () && alphadatamapping.is ("scaled")); }
    std::string get_aliminclude (void) const
      { return aliminclude.current_value (); }

    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  array_property xdata;
  array_property ydata;
  array_property zdata;
  array_property cdata;
  radio_property cdatamapping;
  array_property faces;
  array_property facevertexalphadata;
  array_property facevertexcdata;
  array_property vertices;
  array_property vertexnormals;
  radio_property normalmode;
  color_property facecolor;
  double_radio_property facealpha;
  radio_property facelighting;
  color_property edgecolor;
  double_radio_property edgealpha;
  radio_property edgelighting;
  radio_property backfacelighting;
  double_property ambientstrength;
  double_property diffusestrength;
  double_property specularstrength;
  double_property specularexponent;
  double_property specularcolorreflectance;
  radio_property erasemode;
  radio_property linestyle;
  double_property linewidth;
  radio_property marker;
  color_property markeredgecolor;
  color_property markerfacecolor;
  double_property markersize;
  string_property keylabel;
  radio_property interpreter;
  radio_property alphadatamapping;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  row_vector_property clim;
  row_vector_property alim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property zliminclude;
  bool_property climinclude;
  bool_property aliminclude;

public:

  enum
  {
    XDATA = 7000,
    YDATA = 7001,
    ZDATA = 7002,
    CDATA = 7003,
    CDATAMAPPING = 7004,
    FACES = 7005,
    FACEVERTEXALPHADATA = 7006,
    FACEVERTEXCDATA = 7007,
    VERTICES = 7008,
    VERTEXNORMALS = 7009,
    NORMALMODE = 7010,
    FACECOLOR = 7011,
    FACEALPHA = 7012,
    FACELIGHTING = 7013,
    EDGECOLOR = 7014,
    EDGEALPHA = 7015,
    EDGELIGHTING = 7016,
    BACKFACELIGHTING = 7017,
    AMBIENTSTRENGTH = 7018,
    DIFFUSESTRENGTH = 7019,
    SPECULARSTRENGTH = 7020,
    SPECULAREXPONENT = 7021,
    SPECULARCOLORREFLECTANCE = 7022,
    ERASEMODE = 7023,
    LINESTYLE = 7024,
    LINEWIDTH = 7025,
    MARKER = 7026,
    MARKEREDGECOLOR = 7027,
    MARKERFACECOLOR = 7028,
    MARKERSIZE = 7029,
    KEYLABEL = 7030,
    INTERPRETER = 7031,
    ALPHADATAMAPPING = 7032,
    XLIM = 7033,
    YLIM = 7034,
    ZLIM = 7035,
    CLIM = 7036,
    ALIM = 7037,
    XLIMINCLUDE = 7038,
    YLIMINCLUDE = 7039,
    ZLIMINCLUDE = 7040,
    CLIMINCLUDE = 7041,
    ALIMINCLUDE = 7042
  };

  octave_value get_xdata (void) const { return xdata.get (); }

  octave_value get_ydata (void) const { return ydata.get (); }

  octave_value get_zdata (void) const { return zdata.get (); }

  octave_value get_cdata (void) const { return cdata.get (); }

  bool cdatamapping_is (const std::string& v) const { return cdatamapping.is (v); }
  std::string get_cdatamapping (void) const { return cdatamapping.current_value (); }

  octave_value get_faces (void) const { return faces.get (); }

  octave_value get_facevertexalphadata (void) const { return facevertexalphadata.get (); }

  octave_value get_facevertexcdata (void) const { return facevertexcdata.get (); }

  octave_value get_vertices (void) const { return vertices.get (); }

  octave_value get_vertexnormals (void) const { return vertexnormals.get (); }

  bool normalmode_is (const std::string& v) const { return normalmode.is (v); }
  std::string get_normalmode (void) const { return normalmode.current_value (); }

  bool facecolor_is_rgb (void) const { return facecolor.is_rgb (); }
  bool facecolor_is (const std::string& v) const { return facecolor.is (v); }
  Matrix get_facecolor_rgb (void) const { return (facecolor.is_rgb () ? facecolor.rgb () : Matrix ()); }
  octave_value get_facecolor (void) const { return facecolor.get (); }

  bool facealpha_is_double (void) const { return facealpha.is_double (); }
  bool facealpha_is (const std::string& v) const { return facealpha.is (v); }
  double get_facealpha_double (void) const { return (facealpha.is_double () ? facealpha.double_value () : 0); }
  octave_value get_facealpha (void) const { return facealpha.get (); }

  bool facelighting_is (const std::string& v) const { return facelighting.is (v); }
  std::string get_facelighting (void) const { return facelighting.current_value (); }

  bool edgecolor_is_rgb (void) const { return edgecolor.is_rgb (); }
  bool edgecolor_is (const std::string& v) const { return edgecolor.is (v); }
  Matrix get_edgecolor_rgb (void) const { return (edgecolor.is_rgb () ? edgecolor.rgb () : Matrix ()); }
  octave_value get_edgecolor (void) const { return edgecolor.get (); }

  bool edgealpha_is_double (void) const { return edgealpha.is_double (); }
  bool edgealpha_is (const std::string& v) const { return edgealpha.is (v); }
  double get_edgealpha_double (void) const { return (edgealpha.is_double () ? edgealpha.double_value () : 0); }
  octave_value get_edgealpha (void) const { return edgealpha.get (); }

  bool edgelighting_is (const std::string& v) const { return edgelighting.is (v); }
  std::string get_edgelighting (void) const { return edgelighting.current_value (); }

  bool backfacelighting_is (const std::string& v) const { return backfacelighting.is (v); }
  std::string get_backfacelighting (void) const { return backfacelighting.current_value (); }

  double get_ambientstrength (void) const { return ambientstrength.double_value (); }

  double get_diffusestrength (void) const { return diffusestrength.double_value (); }

  double get_specularstrength (void) const { return specularstrength.double_value (); }

  double get_specularexponent (void) const { return specularexponent.double_value (); }

  double get_specularcolorreflectance (void) const { return specularcolorreflectance.double_value (); }

  bool erasemode_is (const std::string& v) const { return erasemode.is (v); }
  std::string get_erasemode (void) const { return erasemode.current_value (); }

  bool linestyle_is (const std::string& v) const { return linestyle.is (v); }
  std::string get_linestyle (void) const { return linestyle.current_value (); }

  double get_linewidth (void) const { return linewidth.double_value (); }

  bool marker_is (const std::string& v) const { return marker.is (v); }
  std::string get_marker (void) const { return marker.current_value (); }

  bool markeredgecolor_is_rgb (void) const { return markeredgecolor.is_rgb (); }
  bool markeredgecolor_is (const std::string& v) const { return markeredgecolor.is (v); }
  Matrix get_markeredgecolor_rgb (void) const { return (markeredgecolor.is_rgb () ? markeredgecolor.rgb () : Matrix ()); }
  octave_value get_markeredgecolor (void) const { return markeredgecolor.get (); }

  bool markerfacecolor_is_rgb (void) const { return markerfacecolor.is_rgb (); }
  bool markerfacecolor_is (const std::string& v) const { return markerfacecolor.is (v); }
  Matrix get_markerfacecolor_rgb (void) const { return (markerfacecolor.is_rgb () ? markerfacecolor.rgb () : Matrix ()); }
  octave_value get_markerfacecolor (void) const { return markerfacecolor.get (); }

  double get_markersize (void) const { return markersize.double_value (); }

  std::string get_keylabel (void) const { return keylabel.string_value (); }

  bool interpreter_is (const std::string& v) const { return interpreter.is (v); }
  std::string get_interpreter (void) const { return interpreter.current_value (); }

  bool alphadatamapping_is (const std::string& v) const { return alphadatamapping.is (v); }
  std::string get_alphadatamapping (void) const { return alphadatamapping.current_value (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  octave_value get_clim (void) const { return clim.get (); }

  octave_value get_alim (void) const { return alim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }

  bool is_zliminclude (void) const { return zliminclude.is_on (); }
  std::string get_zliminclude (void) const { return zliminclude.current_value (); }


  void set_xdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdata.set (val, true))
          {
            update_xdata ();
            mark_modified ();
          }
      }
  }

  void set_ydata (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydata.set (val, true))
          {
            update_ydata ();
            mark_modified ();
          }
      }
  }

  void set_zdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdata.set (val, true))
          {
            update_zdata ();
            mark_modified ();
          }
      }
  }

  void set_cdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdata.set (val, true))
          {
            update_cdata ();
            mark_modified ();
          }
      }
  }

  void set_cdatamapping (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdatamapping.set (val, false))
          {
            update_axis_limits ("cdatamapping");
            cdatamapping.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_faces (const octave_value& val)
  {
    if (! error_state)
      {
        if (faces.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facevertexalphadata (const octave_value& val)
  {
    if (! error_state)
      {
        if (facevertexalphadata.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facevertexcdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (facevertexcdata.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_vertices (const octave_value& val)
  {
    if (! error_state)
      {
        if (vertices.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_vertexnormals (const octave_value& val)
  {
    if (! error_state)
      {
        if (vertexnormals.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_normalmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (normalmode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (facecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facealpha (const octave_value& val)
  {
    if (! error_state)
      {
        if (facealpha.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (facelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgealpha (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgealpha.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_backfacelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (backfacelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ambientstrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (ambientstrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_diffusestrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (diffusestrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_specularstrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularstrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_specularexponent (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularexponent.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_specularcolorreflectance (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularcolorreflectance.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_erasemode (const octave_value& val)
  {
    if (! error_state)
      {
        if (erasemode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (linestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linewidth (const octave_value& val)
  {
    if (! error_state)
      {
        if (linewidth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_marker (const octave_value& val)
  {
    if (! error_state)
      {
        if (marker.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markeredgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markeredgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markerfacecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markerfacecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markersize (const octave_value& val)
  {
    if (! error_state)
      {
        if (markersize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keylabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (keylabel.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interpreter (const octave_value& val)
  {
    if (! error_state)
      {
        if (interpreter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_alphadatamapping (const octave_value& val)
  {
    if (! error_state)
      {
        if (alphadatamapping.set (val, false))
          {
            update_axis_limits ("alphadatamapping");
            alphadatamapping.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            update_axis_limits ("xlim");
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            update_axis_limits ("ylim");
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, false))
          {
            update_axis_limits ("zlim");
            zlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_clim (const octave_value& val)
  {
    if (! error_state)
      {
        if (clim.set (val, false))
          {
            update_axis_limits ("clim");
            clim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_alim (const octave_value& val)
  {
    if (! error_state)
      {
        if (alim.set (val, false))
          {
            update_axis_limits ("alim");
            alim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, false))
          {
            update_axis_limits ("xliminclude");
            xliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, false))
          {
            update_axis_limits ("yliminclude");
            yliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (zliminclude.set (val, false))
          {
            update_axis_limits ("zliminclude");
            zliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_climinclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (climinclude.set (val, false))
          {
            update_axis_limits ("climinclude");
            climinclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_aliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (aliminclude.set (val, false))
          {
            update_axis_limits ("aliminclude");
            aliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }


  protected:
    void init (void)
      {
	xdata.add_constraint (dim_vector (-1, -1));
	ydata.add_constraint (dim_vector (-1, -1));
	zdata.add_constraint (dim_vector (-1, -1));
        vertices.add_constraint (dim_vector (-1, 2));
        vertices.add_constraint (dim_vector (-1, 3));
	cdata.add_constraint (dim_vector (-1, -1));
	cdata.add_constraint (dim_vector (-1, -1, 3));
	facevertexcdata.add_constraint (dim_vector (-1, 1));
	facevertexcdata.add_constraint (dim_vector (-1, 3));
	facevertexalphadata.add_constraint (dim_vector (-1, 1));
      }

  private:
    void update_xdata (void) { set_xlim (xdata.get_limits ()); }
    void update_ydata (void) { set_ylim (ydata.get_limits ()); }
    void update_zdata (void) { set_zlim (zdata.get_limits ()); }
    
    void update_cdata (void)
      {
	if (cdatamapping_is ("scaled"))
	  set_clim (cdata.get_limits ());
	else
	  clim = cdata.get_limits ();
      }
  };

private:
  properties xproperties;

public:
  patch (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~patch (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
};

// ---------------------------------------------------------------------

class OCTINTERP_API surface : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    octave_value get_color_data (void) const;

    bool is_climinclude (void) const
      { return (climinclude.is_on () && cdatamapping.is ("scaled")); }
    std::string get_climinclude (void) const
      { return climinclude.current_value (); }

    bool is_aliminclude (void) const
      { return (aliminclude.is_on () && alphadatamapping.is ("scaled")); }
    std::string get_aliminclude (void) const
      { return aliminclude.current_value (); }

    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  array_property xdata;
  array_property ydata;
  array_property zdata;
  array_property cdata;
  radio_property cdatamapping;
  string_property xdatasource;
  string_property ydatasource;
  string_property zdatasource;
  string_property cdatasource;
  color_property facecolor;
  double_radio_property facealpha;
  color_property edgecolor;
  radio_property linestyle;
  double_property linewidth;
  radio_property marker;
  color_property markeredgecolor;
  color_property markerfacecolor;
  double_property markersize;
  string_property keylabel;
  radio_property interpreter;
  array_property alphadata;
  radio_property alphadatamapping;
  double_property ambientstrength;
  radio_property backfacelighting;
  double_property diffusestrength;
  double_radio_property edgealpha;
  radio_property edgelighting;
  radio_property erasemode;
  radio_property facelighting;
  radio_property meshstyle;
  radio_property normalmode;
  double_property specularcolorreflectance;
  double_property specularexponent;
  double_property specularstrength;
  array_property vertexnormals;
  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  row_vector_property clim;
  row_vector_property alim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property zliminclude;
  bool_property climinclude;
  bool_property aliminclude;

public:

  enum
  {
    XDATA = 8000,
    YDATA = 8001,
    ZDATA = 8002,
    CDATA = 8003,
    CDATAMAPPING = 8004,
    XDATASOURCE = 8005,
    YDATASOURCE = 8006,
    ZDATASOURCE = 8007,
    CDATASOURCE = 8008,
    FACECOLOR = 8009,
    FACEALPHA = 8010,
    EDGECOLOR = 8011,
    LINESTYLE = 8012,
    LINEWIDTH = 8013,
    MARKER = 8014,
    MARKEREDGECOLOR = 8015,
    MARKERFACECOLOR = 8016,
    MARKERSIZE = 8017,
    KEYLABEL = 8018,
    INTERPRETER = 8019,
    ALPHADATA = 8020,
    ALPHADATAMAPPING = 8021,
    AMBIENTSTRENGTH = 8022,
    BACKFACELIGHTING = 8023,
    DIFFUSESTRENGTH = 8024,
    EDGEALPHA = 8025,
    EDGELIGHTING = 8026,
    ERASEMODE = 8027,
    FACELIGHTING = 8028,
    MESHSTYLE = 8029,
    NORMALMODE = 8030,
    SPECULARCOLORREFLECTANCE = 8031,
    SPECULAREXPONENT = 8032,
    SPECULARSTRENGTH = 8033,
    VERTEXNORMALS = 8034,
    XLIM = 8035,
    YLIM = 8036,
    ZLIM = 8037,
    CLIM = 8038,
    ALIM = 8039,
    XLIMINCLUDE = 8040,
    YLIMINCLUDE = 8041,
    ZLIMINCLUDE = 8042,
    CLIMINCLUDE = 8043,
    ALIMINCLUDE = 8044
  };

  octave_value get_xdata (void) const { return xdata.get (); }

  octave_value get_ydata (void) const { return ydata.get (); }

  octave_value get_zdata (void) const { return zdata.get (); }

  octave_value get_cdata (void) const { return cdata.get (); }

  bool cdatamapping_is (const std::string& v) const { return cdatamapping.is (v); }
  std::string get_cdatamapping (void) const { return cdatamapping.current_value (); }

  std::string get_xdatasource (void) const { return xdatasource.string_value (); }

  std::string get_ydatasource (void) const { return ydatasource.string_value (); }

  std::string get_zdatasource (void) const { return zdatasource.string_value (); }

  std::string get_cdatasource (void) const { return cdatasource.string_value (); }

  bool facecolor_is_rgb (void) const { return facecolor.is_rgb (); }
  bool facecolor_is (const std::string& v) const { return facecolor.is (v); }
  Matrix get_facecolor_rgb (void) const { return (facecolor.is_rgb () ? facecolor.rgb () : Matrix ()); }
  octave_value get_facecolor (void) const { return facecolor.get (); }

  bool facealpha_is_double (void) const { return facealpha.is_double (); }
  bool facealpha_is (const std::string& v) const { return facealpha.is (v); }
  double get_facealpha_double (void) const { return (facealpha.is_double () ? facealpha.double_value () : 0); }
  octave_value get_facealpha (void) const { return facealpha.get (); }

  bool edgecolor_is_rgb (void) const { return edgecolor.is_rgb (); }
  bool edgecolor_is (const std::string& v) const { return edgecolor.is (v); }
  Matrix get_edgecolor_rgb (void) const { return (edgecolor.is_rgb () ? edgecolor.rgb () : Matrix ()); }
  octave_value get_edgecolor (void) const { return edgecolor.get (); }

  bool linestyle_is (const std::string& v) const { return linestyle.is (v); }
  std::string get_linestyle (void) const { return linestyle.current_value (); }

  double get_linewidth (void) const { return linewidth.double_value (); }

  bool marker_is (const std::string& v) const { return marker.is (v); }
  std::string get_marker (void) const { return marker.current_value (); }

  bool markeredgecolor_is_rgb (void) const { return markeredgecolor.is_rgb (); }
  bool markeredgecolor_is (const std::string& v) const { return markeredgecolor.is (v); }
  Matrix get_markeredgecolor_rgb (void) const { return (markeredgecolor.is_rgb () ? markeredgecolor.rgb () : Matrix ()); }
  octave_value get_markeredgecolor (void) const { return markeredgecolor.get (); }

  bool markerfacecolor_is_rgb (void) const { return markerfacecolor.is_rgb (); }
  bool markerfacecolor_is (const std::string& v) const { return markerfacecolor.is (v); }
  Matrix get_markerfacecolor_rgb (void) const { return (markerfacecolor.is_rgb () ? markerfacecolor.rgb () : Matrix ()); }
  octave_value get_markerfacecolor (void) const { return markerfacecolor.get (); }

  double get_markersize (void) const { return markersize.double_value (); }

  std::string get_keylabel (void) const { return keylabel.string_value (); }

  bool interpreter_is (const std::string& v) const { return interpreter.is (v); }
  std::string get_interpreter (void) const { return interpreter.current_value (); }

  octave_value get_alphadata (void) const { return alphadata.get (); }

  bool alphadatamapping_is (const std::string& v) const { return alphadatamapping.is (v); }
  std::string get_alphadatamapping (void) const { return alphadatamapping.current_value (); }

  double get_ambientstrength (void) const { return ambientstrength.double_value (); }

  bool backfacelighting_is (const std::string& v) const { return backfacelighting.is (v); }
  std::string get_backfacelighting (void) const { return backfacelighting.current_value (); }

  double get_diffusestrength (void) const { return diffusestrength.double_value (); }

  bool edgealpha_is_double (void) const { return edgealpha.is_double (); }
  bool edgealpha_is (const std::string& v) const { return edgealpha.is (v); }
  double get_edgealpha_double (void) const { return (edgealpha.is_double () ? edgealpha.double_value () : 0); }
  octave_value get_edgealpha (void) const { return edgealpha.get (); }

  bool edgelighting_is (const std::string& v) const { return edgelighting.is (v); }
  std::string get_edgelighting (void) const { return edgelighting.current_value (); }

  bool erasemode_is (const std::string& v) const { return erasemode.is (v); }
  std::string get_erasemode (void) const { return erasemode.current_value (); }

  bool facelighting_is (const std::string& v) const { return facelighting.is (v); }
  std::string get_facelighting (void) const { return facelighting.current_value (); }

  bool meshstyle_is (const std::string& v) const { return meshstyle.is (v); }
  std::string get_meshstyle (void) const { return meshstyle.current_value (); }

  bool normalmode_is (const std::string& v) const { return normalmode.is (v); }
  std::string get_normalmode (void) const { return normalmode.current_value (); }

  double get_specularcolorreflectance (void) const { return specularcolorreflectance.double_value (); }

  double get_specularexponent (void) const { return specularexponent.double_value (); }

  double get_specularstrength (void) const { return specularstrength.double_value (); }

  octave_value get_vertexnormals (void) const { return vertexnormals.get (); }

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  octave_value get_clim (void) const { return clim.get (); }

  octave_value get_alim (void) const { return alim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }

  bool is_zliminclude (void) const { return zliminclude.is_on (); }
  std::string get_zliminclude (void) const { return zliminclude.current_value (); }


  void set_xdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdata.set (val, true))
          {
            update_xdata ();
            mark_modified ();
          }
      }
  }

  void set_ydata (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydata.set (val, true))
          {
            update_ydata ();
            mark_modified ();
          }
      }
  }

  void set_zdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdata.set (val, true))
          {
            update_zdata ();
            mark_modified ();
          }
      }
  }

  void set_cdata (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdata.set (val, true))
          {
            update_cdata ();
            mark_modified ();
          }
      }
  }

  void set_cdatamapping (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdatamapping.set (val, false))
          {
            update_axis_limits ("cdatamapping");
            cdatamapping.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xdatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (xdatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ydatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (ydatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zdatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (zdatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_cdatasource (const octave_value& val)
  {
    if (! error_state)
      {
        if (cdatasource.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (facecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facealpha (const octave_value& val)
  {
    if (! error_state)
      {
        if (facealpha.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linestyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (linestyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_linewidth (const octave_value& val)
  {
    if (! error_state)
      {
        if (linewidth.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_marker (const octave_value& val)
  {
    if (! error_state)
      {
        if (marker.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markeredgecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markeredgecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markerfacecolor (const octave_value& val)
  {
    if (! error_state)
      {
        if (markerfacecolor.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_markersize (const octave_value& val)
  {
    if (! error_state)
      {
        if (markersize.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_keylabel (const octave_value& val)
  {
    if (! error_state)
      {
        if (keylabel.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_interpreter (const octave_value& val)
  {
    if (! error_state)
      {
        if (interpreter.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_alphadata (const octave_value& val)
  {
    if (! error_state)
      {
        if (alphadata.set (val, true))
          {
            update_alphadata ();
            mark_modified ();
          }
      }
  }

  void set_alphadatamapping (const octave_value& val)
  {
    if (! error_state)
      {
        if (alphadatamapping.set (val, false))
          {
            update_axis_limits ("alphadatamapping");
            alphadatamapping.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ambientstrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (ambientstrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_backfacelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (backfacelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_diffusestrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (diffusestrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgealpha (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgealpha.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_edgelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (edgelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_erasemode (const octave_value& val)
  {
    if (! error_state)
      {
        if (erasemode.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_facelighting (const octave_value& val)
  {
    if (! error_state)
      {
        if (facelighting.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_meshstyle (const octave_value& val)
  {
    if (! error_state)
      {
        if (meshstyle.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_normalmode (const octave_value& val)
  {
    if (! error_state)
      {
        if (normalmode.set (val, true))
          {
            update_normalmode ();
            mark_modified ();
          }
      }
  }

  void set_specularcolorreflectance (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularcolorreflectance.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_specularexponent (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularexponent.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_specularstrength (const octave_value& val)
  {
    if (! error_state)
      {
        if (specularstrength.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_vertexnormals (const octave_value& val)
  {
    if (! error_state)
      {
        if (vertexnormals.set (val, true))
          {
            update_vertexnormals ();
            mark_modified ();
          }
      }
  }

  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, false))
          {
            update_axis_limits ("xlim");
            xlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, false))
          {
            update_axis_limits ("ylim");
            ylim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, false))
          {
            update_axis_limits ("zlim");
            zlim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_clim (const octave_value& val)
  {
    if (! error_state)
      {
        if (clim.set (val, false))
          {
            update_axis_limits ("clim");
            clim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_alim (const octave_value& val)
  {
    if (! error_state)
      {
        if (alim.set (val, false))
          {
            update_axis_limits ("alim");
            alim.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, false))
          {
            update_axis_limits ("xliminclude");
            xliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, false))
          {
            update_axis_limits ("yliminclude");
            yliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_zliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (zliminclude.set (val, false))
          {
            update_axis_limits ("zliminclude");
            zliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_climinclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (climinclude.set (val, false))
          {
            update_axis_limits ("climinclude");
            climinclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }

  void set_aliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (aliminclude.set (val, false))
          {
            update_axis_limits ("aliminclude");
            aliminclude.run_listeners (POSTSET);
            mark_modified ();
          }
      }
  }


  protected:
    void init (void)
      {
	xdata.add_constraint (dim_vector (-1, -1));
	ydata.add_constraint (dim_vector (-1, -1));
	zdata.add_constraint (dim_vector (-1, -1));
	alphadata.add_constraint ("double");
	alphadata.add_constraint ("uint8");
	alphadata.add_constraint (dim_vector (-1, -1));
	vertexnormals.add_constraint (dim_vector (-1, -1, 3));
	cdata.add_constraint ("double");
	cdata.add_constraint ("uint8");
	cdata.add_constraint (dim_vector (-1, -1));
	cdata.add_constraint (dim_vector (-1, -1, 3));
      }

  private:
    void update_normals (void);

    void update_xdata (void)
      {
	update_normals ();
	set_xlim (xdata.get_limits ());
      }
 
    void update_ydata (void)
      {
	update_normals ();
	set_ylim (ydata.get_limits ());
      }

    void update_zdata (void)
      {
	update_normals ();
	set_zlim (zdata.get_limits ());
      }

    void update_cdata (void)
      {
	if (cdatamapping_is ("scaled"))
	  set_clim (cdata.get_limits ());
	else
	  clim = cdata.get_limits ();
      }

    void update_alphadata (void)
      {
	if (alphadatamapping_is ("scaled"))
	  set_alim (alphadata.get_limits ());
	else
	  alim = alphadata.get_limits ();
      }

    void update_normalmode (void)
      { update_normals (); }

    void update_vertexnormals (void)
      { set_normalmode ("manual"); }
  };

private:
  properties xproperties;

public:
  surface (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~surface (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
};

// ---------------------------------------------------------------------

class OCTINTERP_API hggroup : public base_graphics_object
{
public:
  class OCTINTERP_API properties : public base_properties
  {
  public:
    void remove_child (const graphics_handle& h)
      {
	base_properties::remove_child (h);
	update_limits ();
      }

    void adopt (const graphics_handle& h)
      {
	base_properties::adopt (h);
	update_limits ();
      }

    // See the genprops.awk script for an explanation of the
    // properties declarations.

public:
  properties (const graphics_handle& mh, const graphics_handle& p);

  ~properties (void) { }

  void set (const caseless_str& pname, const octave_value& val);

  octave_value get (bool all = false) const;

  octave_value get (const caseless_str& pname) const;

  property get_property (const caseless_str& pname);

  std::string graphics_object_name (void) const { return go_name; }

  static property_list::pval_map_type factory_defaults (void);

private:
  static std::string go_name;

public:


  static bool has_property (const std::string& pname);

private:

  row_vector_property xlim;
  row_vector_property ylim;
  row_vector_property zlim;
  row_vector_property clim;
  row_vector_property alim;
  bool_property xliminclude;
  bool_property yliminclude;
  bool_property zliminclude;
  bool_property climinclude;
  bool_property aliminclude;

public:

  enum
  {
    XLIM = 9000,
    YLIM = 9001,
    ZLIM = 9002,
    CLIM = 9003,
    ALIM = 9004,
    XLIMINCLUDE = 9005,
    YLIMINCLUDE = 9006,
    ZLIMINCLUDE = 9007,
    CLIMINCLUDE = 9008,
    ALIMINCLUDE = 9009
  };

  octave_value get_xlim (void) const { return xlim.get (); }

  octave_value get_ylim (void) const { return ylim.get (); }

  octave_value get_zlim (void) const { return zlim.get (); }

  octave_value get_clim (void) const { return clim.get (); }

  octave_value get_alim (void) const { return alim.get (); }

  bool is_xliminclude (void) const { return xliminclude.is_on (); }
  std::string get_xliminclude (void) const { return xliminclude.current_value (); }

  bool is_yliminclude (void) const { return yliminclude.is_on (); }
  std::string get_yliminclude (void) const { return yliminclude.current_value (); }

  bool is_zliminclude (void) const { return zliminclude.is_on (); }
  std::string get_zliminclude (void) const { return zliminclude.current_value (); }

  bool is_climinclude (void) const { return climinclude.is_on (); }
  std::string get_climinclude (void) const { return climinclude.current_value (); }

  bool is_aliminclude (void) const { return aliminclude.is_on (); }
  std::string get_aliminclude (void) const { return aliminclude.current_value (); }


  void set_xlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (xlim.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_ylim (const octave_value& val)
  {
    if (! error_state)
      {
        if (ylim.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zlim (const octave_value& val)
  {
    if (! error_state)
      {
        if (zlim.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_clim (const octave_value& val)
  {
    if (! error_state)
      {
        if (clim.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_alim (const octave_value& val)
  {
    if (! error_state)
      {
        if (alim.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_xliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (xliminclude.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_yliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (yliminclude.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_zliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (zliminclude.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_climinclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (climinclude.set (val, true))
          {
            mark_modified ();
          }
      }
  }

  void set_aliminclude (const octave_value& val)
  {
    if (! error_state)
      {
        if (aliminclude.set (val, true))
          {
            mark_modified ();
          }
      }
  }


  private:
    void update_limits (void)
      {
	update_axis_limits ("xlim");
	update_axis_limits ("ylim");
	update_axis_limits ("zlim");
	update_axis_limits ("clim");
	update_axis_limits ("alim");
      }

  protected:
    void init (void)
      { }
  };

private:
  properties xproperties;

public:
  hggroup (const graphics_handle& mh, const graphics_handle& p)
    : base_graphics_object (), xproperties (mh, p)
  {
    xproperties.override_defaults (*this);
  }

  ~hggroup (void) { xproperties.delete_children (); }

  base_properties& get_properties (void) { return xproperties; }

  const base_properties& get_properties (void) const { return xproperties; }

  bool valid_object (void) const { return true; }
  
  void update_axis_limits (const std::string& axis_type);
};

// ---------------------------------------------------------------------

octave_value
get_property_from_handle (double handle, const std::string &property,
			  const std::string &func);
bool
set_property_in_handle (double handle, const std::string &property,
			const octave_value &arg, const std::string &func);

// ---------------------------------------------------------------------

class graphics_event;

class
base_graphics_event
{
public:
  friend class graphics_event;

  base_graphics_event (void) : count (1) { }

  virtual ~base_graphics_event (void) { }

  virtual void execute (void) = 0;

private:
  int count;
};

class
graphics_event
{
public:
  typedef void (*event_fcn) (void*);

  graphics_event (void) : rep (0) { }

  graphics_event (const graphics_event& e)
    {
      rep = e.rep;
      rep->count++;
    }

  ~graphics_event (void)
    {
      if (rep && --rep->count == 0)
	delete rep;
    }

  graphics_event& operator = (const graphics_event& e)
    {
      if (rep != e.rep)
	{
	  if (rep && --rep->count == 0)
	    delete rep;

	  rep = e.rep;
	  if (rep)
	    rep->count++;
	}

      return *this;
    }

  void execute (void)
    { if (rep) rep->execute (); }

  bool ok (void) const
    { return (rep != 0); }

  static graphics_event
      create_callback_event (const graphics_handle& h,
			     const std::string& name,
			     const octave_value& data = Matrix ());

  static graphics_event
      create_function_event (event_fcn fcn, void *data = 0);

  static graphics_event
      create_set_event (const graphics_handle& h,
			const std::string& name,
			const octave_value& value);
private:
  base_graphics_event *rep;
};

class OCTINTERP_API gh_manager
{
protected:

  gh_manager (void);

public:

  static bool instance_ok (void)
  {
    bool retval = true;

    if (! instance)
      instance = new gh_manager ();

    if (! instance)
      {
	::error ("unable to create gh_manager!");

	retval = false;
      }

    return retval;
  }

  static void free (const graphics_handle& h)
  {
    if (instance_ok ())
      instance->do_free (h);
  }

  static graphics_handle lookup (double val)
  {
    return instance_ok () ? instance->do_lookup (val) : graphics_handle ();
  }

  static graphics_object get_object (const graphics_handle& h)
  {
    return instance_ok () ? instance->do_get_object (h) : graphics_object ();
  }

  static graphics_handle
  make_graphics_handle (const std::string& go_name,
			const graphics_handle& parent, bool do_createfcn = true)
  {
    return instance_ok ()
      ? instance->do_make_graphics_handle (go_name, parent, do_createfcn)
      : graphics_handle ();
  }

  static graphics_handle make_figure_handle (double val)
  {
    return instance_ok ()
      ? instance->do_make_figure_handle (val) : graphics_handle ();
  }

  static void push_figure (const graphics_handle& h)
  {
    if (instance_ok ())
      instance->do_push_figure (h);
  }

  static void pop_figure (const graphics_handle& h)
  {
    if (instance_ok ())
      instance->do_pop_figure (h);
  }

  static graphics_handle current_figure (void)
  {
    return instance_ok ()
      ? instance->do_current_figure () : graphics_handle ();
  }

  static Matrix handle_list (void)
  {
    return instance_ok () ? instance->do_handle_list () : Matrix ();
  }

  static void lock (void)
  {
    if (instance_ok ())
      instance->do_lock ();
  }

  static void unlock (void)
  {
    if (instance_ok ())
      instance->do_unlock ();
  }

  static Matrix figure_handle_list (void)
  {
    return instance_ok () ? instance->do_figure_handle_list () : Matrix ();
  }

  static void execute_callback (const graphics_handle& h,
				const std::string& name,
				const octave_value& data = Matrix ())
  {
    graphics_object go = get_object (h);

    if (go.valid_object ())
      {
	octave_value cb = go.get (name);

	if (! error_state)
	  execute_callback (h, cb, data);
      }
  }

  static void execute_callback (const graphics_handle& h,
				const octave_value& cb,
				const octave_value& data = Matrix ())
  {
    if (instance_ok ())
      instance->do_execute_callback (h, cb, data);
  }

  static void post_callback (const graphics_handle& h,
			     const std::string& name,
			     const octave_value& data = Matrix ())
  {
    if (instance_ok ())
      instance->do_post_callback (h, name, data);
  }

  static void post_function (graphics_event::event_fcn fcn, void* data = 0)
  {
    if (instance_ok ())
      instance->do_post_function (fcn, data);
  }

  static void post_set (const graphics_handle& h,
			const std::string& name,
			const octave_value& value)
  {
    if (instance_ok ())
      instance->do_post_set (h, name, value);
  }

  static int process_events (void)
  {
    return (instance_ok () ?  instance->do_process_events () : 0);
  }

  static int flush_events (void)
  {
    return (instance_ok () ?  instance->do_process_events (true) : 0);
  }

  static bool is_handle_visible (const graphics_handle& h)
  {
    bool retval = false;

    graphics_object go = get_object (h);

    if (go.valid_object ())
      retval = go.is_handle_visible ();

    return retval;
  }

public:
  class autolock
  {
  public:
    autolock (void) { lock (); }

    ~autolock (void) { unlock (); }

  private:

    // No copying!
    autolock (const autolock&);
    autolock& operator = (const autolock&);
  };

private:

  static gh_manager *instance;

  typedef std::map<graphics_handle, graphics_object>::iterator iterator;
  typedef std::map<graphics_handle, graphics_object>::const_iterator const_iterator;

  typedef std::set<graphics_handle>::iterator free_list_iterator;
  typedef std::set<graphics_handle>::const_iterator const_free_list_iterator;

  typedef std::list<graphics_handle>::iterator figure_list_iterator;
  typedef std::list<graphics_handle>::const_iterator const_figure_list_iterator;

  // A map of handles to graphics objects.
  std::map<graphics_handle, graphics_object> handle_map;

  // The available graphics handles.
  std::set<graphics_handle> handle_free_list;

  // The next handle available if handle_free_list is empty.
  double next_handle;

  // The allocated figure handles.  Top of the stack is most recently
  // created.
  std::list<graphics_handle> figure_list;

  // The lock for accessing the graphics sytsem
  octave_mutex graphics_lock;

  // The list of event queued by backends
  std::list<graphics_event> event_queue;

  // The stack of callback objects
  std::list<graphics_object> callback_objects;

  graphics_handle get_handle (const std::string& go_name);

  void do_free (const graphics_handle& h);

  graphics_handle do_lookup (double val)
  {
    iterator p = (xisnan (val) ? handle_map.end () : handle_map.find (val));

    return (p != handle_map.end ()) ? p->first : graphics_handle ();
  }

  graphics_object do_get_object (const graphics_handle& h)
  {
    iterator p = (h.ok () ? handle_map.find (h) : handle_map.end ());

    return (p != handle_map.end ()) ? p->second : graphics_object ();
  }

  graphics_handle do_make_graphics_handle (const std::string& go_name,
					   const graphics_handle& p, bool do_createfcn);

  graphics_handle do_make_figure_handle (double val);

  Matrix do_handle_list (void)
  {
    Matrix retval (1, handle_map.size ());
    octave_idx_type i = 0;
    for (const_iterator p = handle_map.begin (); p != handle_map.end (); p++)
      {
	graphics_handle h = p->first;
	retval(i++) = h.value ();
      }
    return retval;
  }

  Matrix do_figure_handle_list (void)
  {
    Matrix retval (1, figure_list.size ());
    octave_idx_type i = 0;
    for (const_figure_list_iterator p = figure_list.begin ();
	 p != figure_list.end ();
	 p++)
      {
	graphics_handle h = *p;
	retval(i++) = h.value ();
      }
    return retval;
  }

  void do_push_figure (const graphics_handle& h);

  void do_pop_figure (const graphics_handle& h);

  graphics_handle do_current_figure (void) const
  {
    return figure_list.empty () ? graphics_handle () : figure_list.front ();
  }

  void do_lock (void) { graphics_lock.lock (); }
  
  void do_unlock (void) { graphics_lock.unlock (); }

  void do_execute_callback (const graphics_handle& h, const octave_value& cb,
			    const octave_value& data);

  void do_post_callback (const graphics_handle& h, const std::string name,
			 const octave_value& data);

  void do_post_function (graphics_event::event_fcn fcn, void* fcn_data);

  void do_post_set (const graphics_handle& h, const std::string name,
		    const octave_value& value);

  int do_process_events (bool force = false);

  static void restore_gcbo (void*)
  {
    if (instance_ok ())
      instance->do_restore_gcbo ();
  }

  void do_restore_gcbo (void);

  void do_post_event (const graphics_event& e);
};


// This function is NOT equivalent to the scripting language function gcf.
OCTINTERP_API graphics_handle gcf (void);

// This function is NOT equivalent to the scripting language function gca.
OCTINTERP_API graphics_handle gca (void);

#endif

/*
;;; Local Variables: ***
;;; mode: C++ ***
;;; End: ***
*/
