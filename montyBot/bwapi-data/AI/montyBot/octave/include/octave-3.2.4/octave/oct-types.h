/*

Copyright (C) 2005, 2006, 2007 John W. Eaton

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

#if !defined (octave_oct_types_h)
#define octave_oct_types_h 1

typedef int octave_idx_type;

#if defined (HAVE_STDINT_H)
#include <stdint.h>
#elif defined (HAVE_INTTYPES_H)
#include <inttypes.h>
#else

#if defined (HAVE_LIMITS_H)
#include <limits.h>
#endif

#if CHAR_BIT == 8
typedef signed char int8_t;
typedef unsigned char uint8_t;
#else
#error "CHAR_BIT is not 8!"
#endif

#if SIZEOF_SHORT == 2
typedef short int16_t;
typedef unsigned short uint16_t;
#elif SIZEOF_INT == 2
typedef long int16_t;
typedef unsigned long uint16_t;
#else
#error "No 2 byte integer type found!"
#endif

#if SIZEOF_INT == 4
typedef int int32_t;
typedef unsigned int uint32_t;
#elif SIZEOF_LONG == 4
typedef long int32_t;
typedef unsigned long uint32_t;
#else
#error "No 4 byte integer type found!"
#endif

#if SIZEOF_LONG == 8
typedef long int64_t;
typedef unsigned long uint64_t;
#elif SIZEOF_LONG_LONG == 8
typedef long long int64_t;
typedef unsigned long long uint64_t;
#endif

#endif

#endif

/*
;;; Local Variables: ***
;;; mode: C++ ***
;;; End: ***
*/
