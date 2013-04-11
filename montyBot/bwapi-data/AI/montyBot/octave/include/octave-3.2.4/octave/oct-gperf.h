/* C++ code produced by gperf version 3.0.2 */
/* Command-line: gperf -t -C -D -G -L C++ -Z octave_kw_hash octave.gperf  */
/* Computed positions: -k'1,3' */

#if !((' ' == 32) && ('!' == 33) && ('"' == 34) && ('#' == 35) \
      && ('%' == 37) && ('&' == 38) && ('\'' == 39) && ('(' == 40) \
      && (')' == 41) && ('*' == 42) && ('+' == 43) && (',' == 44) \
      && ('-' == 45) && ('.' == 46) && ('/' == 47) && ('0' == 48) \
      && ('1' == 49) && ('2' == 50) && ('3' == 51) && ('4' == 52) \
      && ('5' == 53) && ('6' == 54) && ('7' == 55) && ('8' == 56) \
      && ('9' == 57) && (':' == 58) && (';' == 59) && ('<' == 60) \
      && ('=' == 61) && ('>' == 62) && ('?' == 63) && ('A' == 65) \
      && ('B' == 66) && ('C' == 67) && ('D' == 68) && ('E' == 69) \
      && ('F' == 70) && ('G' == 71) && ('H' == 72) && ('I' == 73) \
      && ('J' == 74) && ('K' == 75) && ('L' == 76) && ('M' == 77) \
      && ('N' == 78) && ('O' == 79) && ('P' == 80) && ('Q' == 81) \
      && ('R' == 82) && ('S' == 83) && ('T' == 84) && ('U' == 85) \
      && ('V' == 86) && ('W' == 87) && ('X' == 88) && ('Y' == 89) \
      && ('Z' == 90) && ('[' == 91) && ('\\' == 92) && (']' == 93) \
      && ('^' == 94) && ('_' == 95) && ('a' == 97) && ('b' == 98) \
      && ('c' == 99) && ('d' == 100) && ('e' == 101) && ('f' == 102) \
      && ('g' == 103) && ('h' == 104) && ('i' == 105) && ('j' == 106) \
      && ('k' == 107) && ('l' == 108) && ('m' == 109) && ('n' == 110) \
      && ('o' == 111) && ('p' == 112) && ('q' == 113) && ('r' == 114) \
      && ('s' == 115) && ('t' == 116) && ('u' == 117) && ('v' == 118) \
      && ('w' == 119) && ('x' == 120) && ('y' == 121) && ('z' == 122) \
      && ('{' == 123) && ('|' == 124) && ('}' == 125) && ('~' == 126))
/* The character set is not based on ISO-646.  */
#error "gperf generated tables don't work with this execution character set. Please report a bug to <bug-gnu-gperf@gnu.org>."
#endif

#line 1 "octave.gperf"

/*

Copyright (C) 1995, 1997, 1998, 2000, 2002, 2004, 2005, 2006,
              2007, 2008, 2009 John W. Eaton

This file is part of Octave.

Octave is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 3 of the License, or (at
your option) any later version.

Octave is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with Octave; see the file COPYING.  If not, see
<http://www.gnu.org/licenses/>.

NOTE: gperf 2.7.2 will silently generate bad code if there are blank
lines following the "%{" marker above.  This comment block seems to be
handled correctly.

*/
enum octave_kw_id
{
  break_kw,
  case_kw,
  catch_kw,
  continue_kw,
  do_kw,
  else_kw,
  elseif_kw,
  end_kw,
  end_try_catch_kw,
  end_unwind_protect_kw,
  endfor_kw,
  endfunction_kw,
  endif_kw,
  endswitch_kw,
  endwhile_kw,
  for_kw,
  function_kw,
  global_kw,
  if_kw,
  magic_file_kw,
  magic_line_kw,
  otherwise_kw,
  return_kw,
  static_kw,
  switch_kw,
  try_kw,
  until_kw,
  unwind_protect_kw,
  unwind_protect_cleanup_kw,
  while_kw
};
#line 62 "octave.gperf"
struct octave_kw { const char *name; int tok; octave_kw_id kw_id; };

#define TOTAL_KEYWORDS 31
#define MIN_WORD_LENGTH 2
#define MAX_WORD_LENGTH 22
#define MIN_HASH_VALUE 4
#define MAX_HASH_VALUE 53
/* maximum key range = 50, duplicates = 0 */

class octave_kw_hash
{
private:
  static inline unsigned int hash (const char *str, unsigned int len);
public:
  static const struct octave_kw *in_word_set (const char *str, unsigned int len);
};

inline unsigned int
octave_kw_hash::hash (register const char *str, register unsigned int len)
{
  static const unsigned char asso_values[] =
    {
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      25, 54, 54, 54, 54, 54, 20, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 20, 54, 30,  0, 20,
       5,  0, 25, 25,  0, 15, 54, 54, 54, 54,
       0,  0,  0, 54, 15,  0,  5,  5, 54,  0,
      54, 30, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
      54, 54, 54, 54, 54, 54
    };
  register int hval = len;

  switch (hval)
    {
      default:
        hval += asso_values[(unsigned char)str[2]];
      /*FALLTHROUGH*/
      case 2:
      case 1:
        hval += asso_values[(unsigned char)str[0]];
        break;
    }
  return hval;
}

static const struct octave_kw wordlist[] =
  {
#line 69 "octave.gperf"
    {"else", ELSE, else_kw},
#line 64 "octave.gperf"
    {"break", BREAK, break_kw},
#line 70 "octave.gperf"
    {"elseif", ELSEIF, elseif_kw},
#line 68 "octave.gperf"
    {"do", DO, do_kw},
#line 71 "octave.gperf"
    {"end", END, end_kw},
#line 83 "octave.gperf"
    {"otherwise", OTHERWISE, otherwise_kw},
#line 76 "octave.gperf"
    {"endif", END, endif_kw},
#line 74 "octave.gperf"
    {"endfor", END, endfor_kw},
#line 78 "octave.gperf"
    {"endwhile", END, endwhile_kw},
#line 77 "octave.gperf"
    {"endswitch", END, endswitch_kw},
#line 89 "octave.gperf"
    {"until", UNTIL, until_kw},
#line 75 "octave.gperf"
    {"endfunction", END, endfunction_kw},
#line 82 "octave.gperf"
    {"if", IF, if_kw},
#line 72 "octave.gperf"
    {"end_try_catch", END, end_try_catch_kw},
#line 90 "octave.gperf"
    {"unwind_protect", UNWIND, unwind_protect_kw},
#line 92 "octave.gperf"
    {"while", WHILE, while_kw},
#line 87 "octave.gperf"
    {"switch", SWITCH, switch_kw},
#line 73 "octave.gperf"
    {"end_unwind_protect", END, end_unwind_protect_kw},
#line 65 "octave.gperf"
    {"case", CASE, case_kw},
#line 84 "octave.gperf"
    {"persistent", STATIC, static_kw},
#line 85 "octave.gperf"
    {"return", FUNC_RET, return_kw},
#line 91 "octave.gperf"
    {"unwind_protect_cleanup", CLEANUP, unwind_protect_cleanup_kw},
#line 67 "octave.gperf"
    {"continue", CONTINUE, continue_kw},
#line 66 "octave.gperf"
    {"catch", CATCH, catch_kw},
#line 81 "octave.gperf"
    {"global", GLOBAL, global_kw},
#line 80 "octave.gperf"
    {"function", FCN, function_kw},
#line 86 "octave.gperf"
    {"static", STATIC, static_kw},
#line 88 "octave.gperf"
    {"try", TRY, try_kw},
#line 79 "octave.gperf"
    {"for", FOR, for_kw},
#line 94 "octave.gperf"
    {"__LINE__", NUM, magic_line_kw},
#line 93 "octave.gperf"
    {"__FILE__", DQ_STRING, magic_file_kw}
  };

static const signed char gperf_lookup[] =
  {
    -1, -1, -1, -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,  8,
     9, 10, 11, 12, 13, 14, 15, 16, -1, 17, 18, 19, 20, 21,
    22, -1, 23, 24, -1, 25, -1, -1, 26, -1, 27, -1, -1, -1,
    -1, 28, -1, -1, -1, -1, 29, -1, -1, -1, -1, 30
  };

const struct octave_kw *
octave_kw_hash::in_word_set (register const char *str, register unsigned int len)
{
  if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH)
    {
      register int key = hash (str, len);

      if (key <= MAX_HASH_VALUE && key >= 0)
        {
          register int index = gperf_lookup[key];

          if (index >= 0)
            {
              register const char *s = wordlist[index].name;

              if (*str == *s && !strcmp (str + 1, s + 1))
                return &wordlist[index];
            }
        }
    }
  return 0;
}
