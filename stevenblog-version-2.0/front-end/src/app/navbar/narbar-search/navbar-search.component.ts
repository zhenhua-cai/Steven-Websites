import {AfterViewInit, Component, ElementRef, OnInit, Output, ViewChild, EventEmitter} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar-search',
  templateUrl: './navbar-search.component.html',
  styleUrls: ['./navbar-search.component.css']
})
export class NavbarSearchComponent implements OnInit, AfterViewInit {
  searchTitle: string = null;
  @ViewChild('searchArea', {static: true}) searchArea: ElementRef;

  @Output()
  emptyAndLoseFocus = new EventEmitter<boolean>();

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  /**
   * search article by title.
   */
  searchArticle(): void {
    if (!this.validateTitle()) {
      return;
    }
    this.router.navigate([`/articles/search/${this.searchTitle}`]);
  }

  ngAfterViewInit(): void {
    this.searchArea.nativeElement.focus();
  }


  handleLostFocus($event: FocusEvent): void {
    console.log('he');
    if (!this.validateTitle()) {
      this.emptyAndLoseFocus.emit(true);
    }
  }

  validateTitle(): boolean {
    this.searchTitle = this.formatTitle(this.searchTitle);
    return this.searchTitle.length > 0;
  }

  formatTitle(title: string): string {
    if (title == null) {
      return '';
    }
    return title.trim();
  }
}
