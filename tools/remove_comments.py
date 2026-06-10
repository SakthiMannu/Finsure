#!/usr/bin/env python3
"""
Recursively remove comments from files under a target directory.

Usage: python tools/remove_comments.py <target_dir>

Backups of modified files are stored under `comment_removal_backups/<relative_path>`.
Supports common code formats: Java-style (// and /* */), XML/HTML <!-- -->, properties/yaml # and !.
"""
import os
import sys
import shutil
import re
from pathlib import Path

BACKUP_DIR = Path('comment_removal_backups')
SUPPORTED_EXTS = {
    'code': {'.java', '.kt', '.groovy', '.js', '.ts', '.c', '.cpp', '.h', '.cs', '.scala'},
    'xml': {'.xml', '.html', '.xhtml', '.svg', '.md'},
    'props': {'.properties'},
    'yaml': {'.yml', '.yaml'},
}


def remove_comments_code(text: str) -> str:
    # Stateful removal of // and /* */ comments while preserving string and char literals
    n = len(text)
    i = 0
    out = []
    in_single = False
    in_double = False
    in_line_comment = False
    in_block_comment = False

    while i < n:
        ch = text[i]
        nxt = text[i+1] if i+1 < n else ''

        if in_block_comment:
            if ch == '*' and nxt == '/':
                in_block_comment = False
                i += 2
                continue
            else:
                i += 1
                continue

        if in_line_comment:
            if ch == '\n':
                in_line_comment = False
                out.append(ch)
            i += 1
            continue

        # Detect comment starts only when not inside any string/char literal
        if not in_single and not in_double:
            if ch == '/' and nxt == '/':
                in_line_comment = True
                i += 2
                continue
            if ch == '/' and nxt == '*':
                in_block_comment = True
                i += 2
                continue

        # Handle string/char toggling and escapes
        if ch == '"' and not in_single:
            out.append(ch)
            # toggle double unless escaped
            # check previous char escape
            j = len(out)-2
            escaped = False
            while j >= 0 and out[j] == '\\':
                escaped = not escaped
                j -= 1
            if not escaped:
                in_double = not in_double
            i += 1
            continue

        if ch == "'" and not in_double:
            out.append(ch)
            j = len(out)-2
            escaped = False
            while j >= 0 and out[j] == '\\':
                escaped = not escaped
                j -= 1
            if not escaped:
                in_single = not in_single
            i += 1
            continue

        # Preserve escape sequences inside strings
        if ch == '\\' and (in_double or in_single):
            out.append(ch)
            if i+1 < n:
                out.append(text[i+1])
                i += 2
                continue

        out.append(ch)
        i += 1

    return ''.join(out)


def strip_hash_comments_line(line: str) -> str:
    # Remove # comment that is not inside quotes
    in_single = False
    in_double = False
    for i, ch in enumerate(line):
        if ch == '"' and not in_single:
            in_double = not in_double
        elif ch == "'" and not in_double:
            in_single = not in_single
        elif ch == '#' and not in_single and not in_double:
            return line[:i].rstrip()
    return line


def process_file(path: Path, rel_root: Path):
    ext = path.suffix.lower()
    try:
        text = path.read_text(encoding='utf-8')
    except Exception:
        # fallback
        text = path.read_text(encoding='latin-1')

    new_text = text

    if ext in SUPPORTED_EXTS['code']:
        new_text = remove_comments_code(text)
    elif ext in SUPPORTED_EXTS['xml']:
        # Remove XML/HTML/MD <!-- --> comments (multiline)
        new_text = re.sub(r'<!--.*?-->', '', text, flags=re.S)
    elif ext in SUPPORTED_EXTS['props']:
        lines = []
        for ln in text.splitlines(keepends=True):
            stripped = ln.lstrip()
            if stripped.startswith('#') or stripped.startswith('!'):
                # drop the whole line
                continue
            else:
                # remove inline # only if unquoted
                lines.append(strip_hash_comments_line(ln))
        new_text = '\n'.join([l.rstrip('\n') for l in lines])
        if text.endswith('\n'):
            new_text += '\n'
    elif ext in SUPPORTED_EXTS['yaml']:
        lines = []
        for ln in text.splitlines(keepends=False):
            stripped = ln.lstrip()
            if not stripped:
                lines.append('')
                continue
            if stripped.startswith('#'):
                continue
            lines.append(strip_hash_comments_line(ln))
        new_text = '\n'.join(lines)
        if text.endswith('\n'):
            new_text += '\n'

    # Only write if changed
    if new_text != text:
        backup_path = BACKUP_DIR.joinpath(rel_root, path.relative_to(rel_root))
        backup_path.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(path, backup_path)
        path.write_text(new_text, encoding='utf-8')
        return True
    return False


def main():
    if len(sys.argv) < 2:
        print('Usage: python tools/remove_comments.py <target_dir>')
        sys.exit(2)

    target = Path(sys.argv[1])
    if not target.exists() or not target.is_dir():
        print('Target directory does not exist:', target)
        sys.exit(2)

    changed_files = []
    for root, dirs, files in os.walk(target):
        rootp = Path(root)
        for fname in files:
            path = rootp / fname
            # skip backups dir if present
            if 'comment_removal_backups' in path.parts:
                continue
            if path.suffix.lower() in set().union(*SUPPORTED_EXTS.values()):
                try:
                    changed = process_file(path, target)
                    if changed:
                        changed_files.append(str(path))
                except Exception as e:
                    print('Error processing', path, ':', e)

    print('\nComment removal complete.')
    print('Backups saved under', BACKUP_DIR)
    print('Files modified:', len(changed_files))
    for f in changed_files[:200]:
        print(' -', f)


if __name__ == '__main__':
    main()
