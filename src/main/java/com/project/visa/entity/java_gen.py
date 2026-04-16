import re
import sys
import os

def inject_getters_setters(file_path):
    if not os.path.exists(file_path):
        print(f"❌ Erreur : Le fichier '{file_path}' est introuvable.")
        return

    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    content = "".join(lines)
    
    # Extraction des variables privées (Type et Nom)
    fields = re.findall(r'private\s+([\w<>|\[\]]+)\s+(\w+)\s*;', content)
    
    if not fields:
        print(f"⚠️ Aucune variable privée trouvée dans {file_path}.")
        return

    methods_to_add = []
    for java_type, name in fields:
        cap_name = name[0].upper() + name[1:]
        getter_name = f"get{cap_name}"
        setter_name = f"set{cap_name}"
        
        # On n'ajoute que si la méthode n'existe pas déjà
        if getter_name not in content:
            methods_to_add.append(f"\n    public {java_type} {getter_name}() {{\n        return {name};\n    }}")
        
        if setter_name not in content:
            methods_to_add.append(f"\n    public void {setter_name}({java_type} {name}) {{\n        this.{name} = {name};\n    }}")

    if not methods_to_add:
        print(f"ℹ️ Tous les getters/setters sont déjà présents dans {file_path}.")
        return

    # Insertion avant la dernière accolade fermante
    for i in range(len(lines) - 1, -1, -1):
        if '}' in lines[i]:
            lines.insert(i, "\n".join(methods_to_add) + "\n")
            break

    with open(file_path, 'w', encoding='utf-8') as f:
        f.writelines(lines)
    
    print(f"✅ Terminé ! {len(methods_to_add)} méthodes ajoutées dans {file_path}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 generate.py <chemin_du_fichier_java>")
    else:
        inject_getters_setters(sys.argv[1])